package com.itboyst.facedemo.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itboyst.facedemo.dto.FaceDetectResDTO;
import com.itboyst.facedemo.dto.FaceRecognitionResDTO;
import com.itboyst.facedemo.entity.ProcessInfo;
import com.itboyst.facedemo.entity.UserCompareInfo;
import com.itboyst.facedemo.rpc.Response;
import com.itboyst.facedemo.service.FaceEngineService;
import com.itboyst.facedemo.util.Base64Util;
import com.itboyst.facedemo.util.UserRamCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
public class FaceController {

    @Autowired
    private FaceEngineService faceEngineService;

    @Value("${server.port}")
    private int port;


    //初始化注册人脸，注册到本地内存
    @PostConstruct
    public void initFace() throws FileNotFoundException {
        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("zhao1", "赵丽颖");
        fileMap.put("yang1", "杨紫");

        for (String f : fileMap.keySet()) {
            ClassPathResource resource = new ClassPathResource("static/images/" + f +  ".jpg");
            InputStream inputStream = null;
            try {
                inputStream = resource.getInputStream();
            } catch (IOException e) {
            }
            ImageInfo rgbData = ImageFactory.getRGBData(inputStream);
            List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                byte[] feature = faceEngineService.extractFaceFeature(rgbData, faceInfoList.get(0));
                UserRamCache.UserInfo userInfo = new UserCompareInfo();
                userInfo.setFaceId(f);
                userInfo.setName(fileMap.get(f));
                userInfo.setFaceFeature(feature);
                UserRamCache.addUser(userInfo);
            }
        }

        log.info("http://127.0.0.1:" + port + "/");

    }


    /*
    人脸添加
     */
    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
    @ResponseBody
    public Response faceAdd(String file, String faceId, String name) {
        return null;
    }

    /*
    人脸识别
     */
    @RequestMapping(value = "/faceRecognition", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<FaceRecognitionResDTO>> faceRecognition(String image) {

        List<FaceRecognitionResDTO> faceRecognitionResDTOList = Lists.newLinkedList();
        byte[] bytes = Base64Util.base64ToBytes(image);
        ImageInfo rgbData = ImageFactory.getRGBData(bytes);
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
        if (CollectionUtil.isNotEmpty(faceInfoList)) {
            for (FaceInfo faceInfo : faceInfoList) {
                FaceRecognitionResDTO faceRecognitionResDTO = new FaceRecognitionResDTO();
                faceRecognitionResDTO.setRect(faceInfo.getRect());
                byte[] feature = faceEngineService.extractFaceFeature(rgbData, faceInfo);
                if (feature != null) {
                    List<UserCompareInfo> userCompareInfos = faceEngineService.faceRecognition(feature, UserRamCache.getUserList(), 0.8f);
                    if (CollectionUtil.isNotEmpty(userCompareInfos)) {
                        faceRecognitionResDTO.setName(userCompareInfos.get(0).getName());
                        faceRecognitionResDTO.setSimilar(userCompareInfos.get(0).getSimilar());
                    }
                }
                faceRecognitionResDTOList.add(faceRecognitionResDTO);
            }

        }


        return Response.newSuccessResponse(faceRecognitionResDTOList);
    }


    @RequestMapping(value = "/detectFaces", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<FaceDetectResDTO>> detectFaces(String image) {

        byte[] bytes = Base64Util.base64ToBytes(image);
        ImageInfo rgbData = ImageFactory.getRGBData(bytes);
        List<FaceDetectResDTO> faceDetectResDTOS = Lists.newLinkedList();
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
        if (CollectionUtil.isNotEmpty(faceInfoList)) {
            List<ProcessInfo> process = faceEngineService.process(rgbData, faceInfoList);

            for (int i = 0; i < faceInfoList.size(); i++) {
                FaceDetectResDTO faceDetectResDTO = new FaceDetectResDTO();
                FaceInfo faceInfo = faceInfoList.get(i);
                faceDetectResDTO.setRect(faceInfo.getRect());
                faceDetectResDTO.setOrient(faceInfo.getOrient());
                faceDetectResDTO.setFaceId(faceInfo.getFaceId());
                if (CollectionUtil.isNotEmpty(process)) {
                    ProcessInfo processInfo = process.get(i);
                    faceDetectResDTO.setAge(processInfo.getAge());
                    faceDetectResDTO.setGender(processInfo.getGender());
                    faceDetectResDTO.setLiveness(processInfo.getLiveness());

                }
                faceDetectResDTOS.add(faceDetectResDTO);

            }
        }

        return Response.newSuccessResponse(faceDetectResDTOS);
    }

    @RequestMapping(value = "/compareFaces", method = RequestMethod.POST)
    @ResponseBody
    public Response<Float> compareFaces(String image1, String image2) {

        byte[] bytes1 = Base64Util.base64ToBytes(image1);
        byte[] bytes2 = Base64Util.base64ToBytes(image2);
        ImageInfo rgbData1 = ImageFactory.getRGBData(bytes1);
        ImageInfo rgbData2 = ImageFactory.getRGBData(bytes2);

        Float similar = faceEngineService.compareFace(rgbData1, rgbData2);

        return Response.newSuccessResponse(similar);
    }

}
