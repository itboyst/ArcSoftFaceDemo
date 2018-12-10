package com.arcsoft.facedemo.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.arcsoft.facedemo.domain.UserFaceInfo;
import com.arcsoft.facedemo.dto.FaceSearchResDto;
import com.arcsoft.facedemo.dto.ProcessInfo;
import com.arcsoft.facedemo.service.FaceEngineService;
import com.arcsoft.facedemo.service.UserFaceInfoService;
import com.arcsoft.facedemo.dto.FaceUserInfo;
import com.arcsoft.facedemo.base.ImageInfo;
import com.arcsoft.facedemo.base.Result;
import com.arcsoft.facedemo.base.Results;
import com.arcsoft.facedemo.enums.ErrorCodeEnum;
import com.arcsoft.facedemo.util.ImageUtil;
import com.arcsoft.face.FaceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;


@Controller
public class FaceController {

    public final static Logger logger = LoggerFactory.getLogger(FaceController.class);


    @Autowired
    FaceEngineService faceEngineService;

    @Autowired
    UserFaceInfoService userFaceInfoService;

    @RequestMapping(value = "/demo")
    public String demo() {
        return "demo";
    }


    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> faceAdd(@RequestParam("file") MultipartFile file, @RequestParam("groupId") Integer groupId, @RequestParam("name") String name) {
        try {
            if (file == null) {
                return Results.newFailedResult("file is null");
            }
            if (groupId == null) {
                return Results.newFailedResult("groupId is null");
            }
            if (name == null) {
                return Results.newFailedResult("name is null");
            }

            InputStream inputStream = file.getInputStream();
            ImageInfo imageInfo = ImageUtil.getRGBData(inputStream);

            //人脸特征获取
            byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
            if (bytes == null) {
                return Results.newFailedResult(ErrorCodeEnum.NO_FACE_DETECTED);
            }

            UserFaceInfo userFaceInfo = new UserFaceInfo();
            userFaceInfo.setName(name);
            userFaceInfo.setGroupId(groupId);
            userFaceInfo.setFaceFeature(bytes);
            userFaceInfo.setFaceId(RandomUtil.randomString(10));

            //人脸特征插入到数据库
            userFaceInfoService.insertSelective(userFaceInfo);
            FaceUserInfo faceUserInfo = new FaceUserInfo();
            faceUserInfo.setName(name);
            faceUserInfo.setFaceId(userFaceInfo.getFaceId());
            faceUserInfo.setFaceFeature(bytes);
            //人脸信息添加到缓存
            faceEngineService.addFaceToCache(groupId, faceUserInfo);

            logger.info("faceAdd:" + name);
            return Results.newSuccessResult("");
        } catch (Exception e) {
            logger.error("", e);
        }
        return Results.newFailedResult(ErrorCodeEnum.UNKNOWN);
    }

    @RequestMapping(value = "/faceSearch", method = RequestMethod.POST)
    @ResponseBody
    public Result<FaceSearchResDto> faceSearch(MultipartFile file, Integer groupId) throws Exception {

        if (groupId == null) {
            return Results.newFailedResult("groupId is null");
        }

        InputStream inputStream = file.getInputStream();
        BufferedImage bufImage = ImageIO.read(inputStream);
        ImageInfo imageInfo = ImageUtil.bufferedImage2ImageInfo(bufImage);
        if (inputStream != null) {
            inputStream.close();
        }


        //人脸特征获取
        byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);


        if (bytes == null) {
            return Results.newFailedResult(ErrorCodeEnum.NO_FACE_DETECTED);
        }
        //人脸比对，获取比对结果
        List<FaceUserInfo> userFaceInfoList = faceEngineService.compareFaceFeature(bytes, groupId);


        if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
            FaceUserInfo faceUserInfo = userFaceInfoList.get(0);
            FaceSearchResDto faceSearchResDto = new FaceSearchResDto();
            BeanUtil.copyProperties(faceUserInfo, faceSearchResDto);
            List<ProcessInfo> processInfoList = faceEngineService.process(imageInfo);
            if (CollectionUtil.isNotEmpty(processInfoList)) {
                //人脸检测
                List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);
                int left = faceInfoList.get(0).getRect().getLeft();
                int top = faceInfoList.get(0).getRect().getTop();
                int width = faceInfoList.get(0).getRect().getRight() - left;
                int height = faceInfoList.get(0).getRect().getBottom()- top;

                Graphics2D graphics2D = bufImage.createGraphics();
                graphics2D.setColor(Color.RED);//红色
                BasicStroke stroke = new BasicStroke(5f);
                graphics2D.setStroke(stroke);
                graphics2D.drawRect(left, top, width, height);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufImage, "jpg", outputStream);
                byte[] bytes1 = outputStream.toByteArray();
                faceSearchResDto.setImage("data:image/jpeg;base64," + Base64Utils.encodeToString(bytes1));
                faceSearchResDto.setAge(processInfoList.get(0).getAge());
                faceSearchResDto.setGender(processInfoList.get(0).getGender().equals(1) ? "女" : "男");

            }

            return Results.newSuccessResult(faceSearchResDto);
        }


        return Results.newFailedResult(ErrorCodeEnum.FACE_DOES_NOT_MATCH);
    }


    @RequestMapping(value = "/detectFaces", method = RequestMethod.POST)
    @ResponseBody
    public List<FaceInfo> detectFaces(String image) throws IOException {
        byte[] bytes = Base64Utils.decodeFromString(image.trim());

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ImageInfo imageInfo = ImageUtil.getRGBData(inputStream);

        if (inputStream != null) {
            inputStream.close();
        }
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);

        return faceInfoList;
    }

}
