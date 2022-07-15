package com.itboyst.face.face;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ExtractType;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.itboyst.face.config.ArcFaceAutoConfiguration;
import com.itboyst.face.factory.FaceEngineFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author st7251
 * @date 2020/3/2 16:17
 */
@Slf4j
public class FaceRecognize {

    /**
     * VIDEO模式人脸检测引擎，用于预览帧人脸追踪
     */
    private FaceEngine ftEngine;

    /**
     * 人脸注册引擎
     */
    private FaceEngine regEngine;

    /**
     * 用于人脸识别的引擎池
     */
    private GenericObjectPool<FaceEngine> frEnginePool;


    private volatile ConcurrentHashMap<Integer, FaceResult> faceResultRegistry = new ConcurrentHashMap<>();

    private ExecutorService frService = Executors.newFixedThreadPool(20);

    public ConcurrentHashMap<String, byte[]> faceFeatureRegistry = new ConcurrentHashMap<>();

    /**
     * 初始化引擎
     */
    public void initEngine(String appId, String sdkKey, String activeKey, String activeFile) {

        //引擎配置
        ftEngine = new FaceEngine(ArcFaceAutoConfiguration.CACHE_LIB_FOLDER);
        int activeCode;
        if (StringUtils.isNotEmpty(activeFile)) {
            activeCode = ftEngine.activeOffline(activeFile);
        } else {
            activeCode = ftEngine.activeOnline(appId, sdkKey, activeKey);
        }

        EngineConfiguration ftEngineCfg = new EngineConfiguration();
        ftEngineCfg.setDetectMode(DetectMode.ASF_DETECT_MODE_VIDEO);
        ftEngineCfg.setFunctionConfiguration(FunctionConfiguration.builder().supportFaceDetect(true).build());
        int ftInitCode = ftEngine.init(ftEngineCfg);

        //引擎配置
        regEngine = new FaceEngine(ArcFaceAutoConfiguration.CACHE_LIB_FOLDER);

        EngineConfiguration regEngineCfg = new EngineConfiguration();
        regEngineCfg.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        regEngineCfg.setFunctionConfiguration(FunctionConfiguration.builder().supportFaceDetect(true).supportFaceRecognition(true).build());
        int regInitCode = regEngine.init(regEngineCfg);


        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(5);
        poolConfig.setMaxTotal(5);
        poolConfig.setMinIdle(5);
        poolConfig.setLifo(false);
        EngineConfiguration frEngineCfg = new EngineConfiguration();
        frEngineCfg.setFunctionConfiguration(FunctionConfiguration.builder().supportFaceRecognition(true).build());
        frEnginePool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, activeKey,activeFile, frEngineCfg), poolConfig);//底层库算法对象池


        if (!(activeCode == ErrorInfo.MOK.getValue() || activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue())) {
            log.error("activeCode: " + activeCode);
            throw new RuntimeException("activeCode: " + activeCode);
        }
        if (ftInitCode != ErrorInfo.MOK.getValue()) {
            log.error("ftInitEngine: " + ftInitCode);
            throw new RuntimeException("ftInitEngine: " + ftInitCode);
        }

        if (regInitCode != ErrorInfo.MOK.getValue()) {
            log.error("regInitEngine: " + regInitCode);
            throw new RuntimeException("regInitEngine: " + regInitCode);
        }

    }


    public void registerFace(String imagePath) {

        log.info("正在注册人脸");

        int count = 0;
        if (regEngine != null) {
            File file = new File(imagePath);
            File[] files = file.listFiles();

            for (File file1 : files) {
                ImageInfo imageInfo = ImageFactory.getRGBData(file1);
                if (imageInfo != null) {
                    List<FaceInfo> faceInfoList = new ArrayList<>();
                    int code = regEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
                            imageInfo.getImageFormat(), faceInfoList);

                    if (code == 0 && faceInfoList.size() > 0) {
                        FaceFeature faceFeature = new FaceFeature();
                        int resCode = regEngine.extractFaceFeature(imageInfo, faceInfoList.get(0), ExtractType.REGISTER, 0, faceFeature);
                        if (resCode == 0) {
                            int lastIndexOf = file1.getName().lastIndexOf(".");
                            String name = file1.getName().substring(0, file1.getName().length() - lastIndexOf - 1);
                            faceFeatureRegistry.put(name, faceFeature.getFeatureData());
                            log.info("成功注册人脸：" + name);
                            count++;
                        }
                    }
                }
            }
            log.info("人脸注册完成，共注册：" + count + "张人脸");
        } else {
            throw new RuntimeException("注册失败，引擎未初始化或初始化失败");
        }


    }

    public void registerFace(Map<String, byte[]> face) {
        face.forEach((k, v) -> {
            faceFeatureRegistry.put(k, v.clone());
        });
    }

    public void removeFace(String name) {
        faceFeatureRegistry.remove(name);
    }

    public void clearFace() {
        faceFeatureRegistry.clear();
    }

    public FaceResult getFaceResult(FaceInfo faceInfo, ImageInfo imageInfo) {
        FaceResult faceResult = faceResultRegistry.get(faceInfo.getFaceId());
        if (faceResult == null) {
            faceResult = new FaceResult();
            faceResultRegistry.put(faceInfo.getFaceId(), faceResult);
            frService.submit(new FaceInfoRunnable(faceInfo, imageInfo, faceResult));
        } else if (faceResult.isFlag()) {
            return faceResult;
        }
        return null;
    }

    public List<FacePreviewInfo> detectFaces(ImageInfo imageInfo) {
        if (ftEngine != null) {
            List<FaceInfo> faceInfoList = new ArrayList<>();
            int code = ftEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
                    imageInfo.getImageFormat(), faceInfoList);

            List<FacePreviewInfo> previewInfoList = new LinkedList<>();
            for (FaceInfo faceInfo : faceInfoList) {
                FacePreviewInfo facePreviewInfo = new FacePreviewInfo();
                facePreviewInfo.setFaceInfo(faceInfo);
                previewInfoList.add(facePreviewInfo);
            }

            clearFaceResultRegistry(faceInfoList);
            return previewInfoList;

        }
        return null;
    }


    private long lastClearTime = System.currentTimeMillis();

    //清理过时的人脸
    private void clearFaceResultRegistry(List<FaceInfo> faceInfoList) {
        if (System.currentTimeMillis() - lastClearTime > 5000) {
            Iterator<Integer> iterator = faceResultRegistry.keySet().iterator();
            for (; iterator.hasNext(); ) {
                Integer next = iterator.next();
                boolean flag = false;
                for (FaceInfo faceInfo : faceInfoList) {
                    if (next.equals(faceInfo.getFaceId())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    iterator.remove();
                }

            }
            lastClearTime = System.currentTimeMillis();
        }


    }


    @Data
    public class FaceResult {
        private boolean flag = false;
        private String name;
        private float score;


    }

    @Data
    public class FacePreviewInfo {

        private FaceInfo faceInfo;
        private int age;
        private boolean liveness;

    }


    private class FaceInfoRunnable implements Runnable {
        private FaceInfo faceInfo;
        private ImageInfo imageInfo;
        private FaceResult faceResult;

        public FaceInfoRunnable(FaceInfo faceInfo, ImageInfo imageInfo, FaceResult faceResult) {
            this.faceInfo = faceInfo;
            this.imageInfo = imageInfo;
            this.faceResult = faceResult;
        }

        @Override
        public void run() {
            FaceEngine frEngine = null;
            try {
                frEngine = frEnginePool.borrowObject();
                if (frEngine != null) {
                    FaceFeature faceFeature = new FaceFeature();
                    int resCode = frEngine.extractFaceFeature(imageInfo, faceInfo, ExtractType.RECOGNIZE, 0, faceFeature);
                    if (resCode == 0) {

                        float score = 0.0F;
                        Iterator<Map.Entry<String, byte[]>> iterator = faceFeatureRegistry.entrySet().iterator();
                        for (; iterator.hasNext(); ) {
                            Map.Entry<String, byte[]> next = iterator.next();
                            FaceFeature faceFeatureTarget = new FaceFeature();
                            faceFeatureTarget.setFeatureData(next.getValue());

                            FaceSimilar faceSimilar = new FaceSimilar();
                            frEngine.compareFaceFeature(faceFeatureTarget, faceFeature, faceSimilar);
                            if (faceSimilar.getScore() > score) {
                                score = faceSimilar.getScore();
                                faceResult.setName(next.getKey());
                            }
                        }

                        log.info("相似度：" + score);
                        if (score >= 0.8f) {
                            faceResult.setScore(score);
                            faceResult.setFlag(true);
                            faceResultRegistry.put(faceInfo.getFaceId(), faceResult);
                        } else {
                            faceResultRegistry.remove(faceInfo.getFaceId());
                        }

                    }
                }
            } catch (Exception e) {

            } finally {
                if (frEngine != null) {
                    frEnginePool.returnObject(frEngine);
                }
            }


        }
    }


}
