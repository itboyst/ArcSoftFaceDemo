package com.itboyst.face.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.enums.ExtractType;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itboyst.face.dto.*;
import com.itboyst.face.entity.ProcessInfo;
import com.itboyst.face.entity.UserCompareInfo;
import com.itboyst.face.rpc.Response;
import com.itboyst.face.service.FaceEngineService;
import com.itboyst.face.util.Base64Util;
import com.itboyst.face.util.UserRamCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
public class FaceController {

    @Autowired
    private FaceEngineService faceEngineService;


    //初始化注册人脸，注册到本地内存
    @PostConstruct
    public void initFace() throws Exception {
        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("zhao1", "赵丽颖");
        fileMap.put("yang1", "杨紫");
        fileMap.put("baixue", "白雪");
        fileMap.put("chenchuang", "陈创");
        for (String f : fileMap.keySet()) {
            ClassPathResource resource = new ClassPathResource("static/images/" + f + ".jpg");
            InputStream inputStream = resource.getInputStream();
            ImageInfo rgbData = ImageFactory.getRGBData(inputStream);
            List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                byte[] feature = faceEngineService.extractFaceFeature(rgbData, faceInfoList.get(0), ExtractType.REGISTER);
                UserRamCache.UserInfo userInfo = new UserCompareInfo();
                userInfo.setFaceId(f);
                userInfo.setName(fileMap.get(f));
                userInfo.setFaceFeature(feature);
                //这边注册到内存缓存中，也可以根据业务，注册到数据库中
                UserRamCache.addUser(userInfo);
            }
        }



    }


    /*
    人脸添加
     */
    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
    @ResponseBody
    public Response faceAdd(@RequestBody FaceAddReqDTO faceAddReqDTO) {
        String image = faceAddReqDTO.getImage();

        byte[] bytes = Base64Util.base64ToBytes(image);
        ImageInfo rgbData = ImageFactory.getRGBData(bytes);
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
        if (CollectionUtil.isNotEmpty(faceInfoList)) {
            for (FaceInfo faceInfo : faceInfoList) {
                FaceRecognitionResDTO faceRecognitionResDTO = new FaceRecognitionResDTO();
                faceRecognitionResDTO.setRect(faceInfo.getRect());
                byte[] feature = faceEngineService.extractFaceFeature(rgbData, faceInfo,ExtractType.REGISTER);
                if (feature != null) {
                    UserRamCache.UserInfo userInfo = new UserCompareInfo();
                    userInfo.setFaceId(faceAddReqDTO.getName());
                    userInfo.setName(faceAddReqDTO.getName());
                    userInfo.setFaceFeature(feature);
                    //这边注册到内存缓存中，也可以根据业务，注册到数据库中
                    UserRamCache.addUser(userInfo);
                }

            }

        }
        return Response.newSuccessResponse("");
    }


    @RequestMapping(value = "/getFaceList", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<GetFaceListResDTO>> getFaceList() {
        List<UserRamCache.UserInfo> userList = UserRamCache.getUserList();
        List<GetFaceListResDTO> resDTOS = new LinkedList<>();
        for (UserRamCache.UserInfo userInfo : userList) {
            GetFaceListResDTO face = new GetFaceListResDTO();
            face.setId(userInfo.getFaceId());
            face.setName(userInfo.getName());
            face.setUrl("/images/" + face.getId() + ".jpg");
            resDTOS.add(face);
        }
        return Response.newSuccessResponse(resDTOS);
    }


    /*
    人脸识别
     */
    @RequestMapping(value = "/faceRecognition", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<FaceRecognitionResDTO>> faceRecognition(@RequestBody FaceRecognitionReqDTO faceRecognitionReqDTO) {
        String image = faceRecognitionReqDTO.getImage();

        List<FaceRecognitionResDTO> faceRecognitionResDTOList = Lists.newLinkedList();
        byte[] bytes = Base64Util.base64ToBytes(image);
        ImageInfo rgbData = ImageFactory.getRGBData(bytes);
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
        if (CollectionUtil.isNotEmpty(faceInfoList)) {
            for (FaceInfo faceInfo : faceInfoList) {
                FaceRecognitionResDTO faceRecognitionResDTO = new FaceRecognitionResDTO();
                faceRecognitionResDTO.setRect(faceInfo.getRect());
                byte[] feature = faceEngineService.extractFaceFeature(rgbData, faceInfo,ExtractType.RECOGNIZE);
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
    public Response<List<FaceDetectResDTO>> detectFaces(@RequestBody FaceDetectReqDTO faceDetectReqDTO) {
        String image = faceDetectReqDTO.getImage();
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
    public Response<Float> compareFaces(@RequestBody CompareFacesReqDTO compareFacesReqDTO) {

        String image1 = compareFacesReqDTO.getImage1();
        String image2 = compareFacesReqDTO.getImage2();

        byte[] bytes1 = Base64Util.base64ToBytes(image1);
        byte[] bytes2 = Base64Util.base64ToBytes(image2);
        ImageInfo rgbData1 = ImageFactory.getRGBData(bytes1);
        ImageInfo rgbData2 = ImageFactory.getRGBData(bytes2);

        Float similar = faceEngineService.compareFace(rgbData1, rgbData2);

        return Response.newSuccessResponse(similar);
    }


}
