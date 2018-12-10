package com.arcsoft.facedemo.factory;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @Author: st7251
 * @Date: 2018/10/16 13:47
 */
public class FaceEngineFactory extends BasePooledObjectFactory<FaceEngine> {

    private String appId;
    private String sdkKey;
    private FunctionConfiguration functionConfiguration;
    private Integer detectFaceMaxNum=10;
    private Integer detectFaceScaleVal=16;
    private DetectMode detectMode= DetectMode.ASF_DETECT_MODE_IMAGE;
    private DetectOrient detectFaceOrientPriority= DetectOrient.ASF_OP_0_HIGHER_EXT;


    public FaceEngineFactory(String appId, String sdkKey, FunctionConfiguration functionConfiguration) {
        this.appId = appId;
        this.sdkKey = sdkKey;
        this.functionConfiguration = functionConfiguration;
    }



    @Override
    public FaceEngine create() throws Exception {

        EngineConfiguration engineConfiguration= EngineConfiguration.builder()
                .functionConfiguration(functionConfiguration)
                .detectFaceMaxNum(detectFaceMaxNum)
                .detectFaceScaleVal(detectFaceScaleVal)
                .detectMode(detectMode)
                .detectFaceOrientPriority(detectFaceOrientPriority)
                .build();
        FaceEngine faceEngine =new FaceEngine();
        faceEngine.active(appId,sdkKey);
        faceEngine.init(engineConfiguration);

        return faceEngine;
    }

    @Override
    public PooledObject<FaceEngine> wrap(FaceEngine faceEngine) {
        return new DefaultPooledObject<>(faceEngine);
    }


    @Override
    public void destroyObject(PooledObject<FaceEngine> p) throws Exception {
        FaceEngine faceEngine = p.getObject();
        int result = faceEngine.unInit();
        super.destroyObject(p);
    }
}
