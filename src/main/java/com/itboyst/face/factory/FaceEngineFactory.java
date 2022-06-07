package com.itboyst.face.factory;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.enums.ErrorInfo;
import com.itboyst.face.config.ArcFaceAutoConfiguration;
import com.itboyst.face.enums.ErrorCodeEnum;
import com.itboyst.face.rpc.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

@Slf4j
public class FaceEngineFactory extends BasePooledObjectFactory<FaceEngine> {

    private String appId;
    private String sdkKey;
    private String activeKey;
    private EngineConfiguration engineConfiguration;


    public FaceEngineFactory( String appId, String sdkKey, String activeKey, EngineConfiguration engineConfiguration) {
        this.appId = appId;
        this.sdkKey = sdkKey;
        this.activeKey = activeKey;
        this.engineConfiguration = engineConfiguration;
    }


    @Override
    public FaceEngine create() {


        FaceEngine faceEngine = new FaceEngine(ArcFaceAutoConfiguration.CACHE_LIB_FOLDER);
        int activeCode = faceEngine.activeOnline(appId, sdkKey);
        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.error("引擎激活失败" + activeCode);
            throw new BusinessException(ErrorCodeEnum.FAIL, "引擎激活失败" + activeCode);
        }
        int initCode = faceEngine.init(engineConfiguration);
        if (initCode != ErrorInfo.MOK.getValue()) {
            log.error("引擎初始化失败" + initCode);
            throw new BusinessException(ErrorCodeEnum.FAIL, "引擎初始化失败" + initCode);
        }
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
