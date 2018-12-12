package com.itboyst.facedemo.enums;



public enum ErrorCodeEnum {

    MOK(0,"成功"),
    UNKNOWN(1,"未知错误"),
    INVALID_PARAM(2,"无效参数"),
    UNSUPPORTED(3,"引擎不支持"),
    NO_MEMORY(4,"内存不足"),
    BAD_STATE(5,"状态错误"),
    USER_CANCEL(6,"用户取消相关操作"),
    EXPIRED(7,"操作时间过期"),
    USER_PAUSE(8,"用户暂停操作"),
    BUFFER_OVERFLOW(9,"缓冲上溢"),
    BUFFER_UNDERFLOW(10,"缓冲下溢"),
    NO_DISKSPACE(11,"存贮空间不足"),
    COMPONENT_NOT_EXIST(12,"组件不存在"),
    GLOBAL_DATA_NOT_EXIST(13,"全局数据不存在"),
    NO_FACE_DETECTED(14,"未检出到人脸"),
    FACE_DOES_NOT_MATCH(15,"人脸不匹配"),
    INVALID_APP_ID(28673,"无效的AppId"),
    INVALID_SDK_ID(28674,"无效的SdkKey"),
    INVALID_ID_PAIR(28675,"AppId和SdkKey不匹配"),
    MISMATCH_ID_AND_SDK(28676,"SdkKey 和使用的SDK 不匹配"),
    SYSTEM_VERSION_UNSUPPORTED(28677,"系统版本不被当前SDK所支持"),
    LICENCE_EXPIRED(28678,"SDK有效期过期，需要重新下载更新"),
    APS_ENGINE_HANDLE(69633,"引擎句柄非法"),
    APS_MEMMGR_HANDLE(69634,"内存句柄非法"),
    APS_DEVICEID_INVALID(69635," Device ID 非法"),
    APS_DEVICEID_UNSUPPORTED(69636,"Device ID 不支持"),
    APS_MODEL_HANDLE(69637,"模板数据指针非法"),
    APS_MODEL_SIZE(69638,"模板数据长度非法"),
    APS_IMAGE_HANDLE(69639,"图像结构体指针非法"),
    APS_IMAGE_FORMAT_UNSUPPORTED(69640,"图像格式不支持"),
    APS_IMAGE_PARAM(69641,"图像参数非法"),
    APS_IMAGE_SIZE(69642,"图像尺寸大小超过支持范围"),
    APS_DEVICE_AVX2_UNSUPPORTED(69643,"处理器不支持AVX2指令"),
    FR_INVALID_MEMORY_INFO(73729,"无效的输入内存"),
    FR_INVALID_IMAGE_INFO(73730,"无效的输入图像参数"),
    FR_INVALID_FACE_INFO(73731,"无效的脸部信息"),
    FR_NO_GPU_AVAILABLE(73732,"当前设备无GPU可用"),
    FR_MISMATCHED_FEATURE_LEVEL(73733,"待比较的两个人脸特征的版本不一致"),
    FACEFEATURE_UNKNOWN(81921,"人脸特征检测错误未知"),
    FACEFEATURE_MEMORY(81922,"人脸特征检测内存错误"),
    FACEFEATURE_INVALID_FORMAT(81923,"人脸特征检测格式错误"),
    FACEFEATURE_INVALID_PARAM(81924,"人脸特征检测参数错误"),
    FACEFEATURE_LOW_CONFIDENCE_LEVEL(81925,"人脸特征检测结果置信度低"),
    ASF_EX_BASE_FEATURE_UNSUPPORTED_ON_INIT(86017,"Engine不支持的检测属性"),
    ASF_EX_BASE_FEATURE_UNINITED(86018,"需要检测的属性未初始化"),
    ASF_EX_BASE_FEATURE_UNPROCESSED(86019,"待获取的属性未在process中处理过"),
    ASF_EX_BASE_FEATURE_UNSUPPORTED_ON_PROCESS(86020,"PROCESS不支持的检测属性，例如FR，有自己独立的处理函数"),
    ASF_EX_BASE_INVALID_IMAGE_INFO(86021,"无效的输入图像"),
    ASF_EX_BASE_INVALID_FACE_INFO(86022,"无效的脸部信息"),
    ASF_BASE_ACTIVATION_FAIL(90113,"人脸比对SDK激活失败,请打开读写权限"),
    ASF_BASE_ALREADY_ACTIVATED(90114,"人脸比对SDK已激活"),
    ASF_BASE_NOT_ACTIVATED(90115,"人脸比对SDK未激活"),
    ASF_BASE_SCALE_NOT_SUPPORT(90116,"detectFaceScaleVal 不支持"),
    ASF_BASE_VERION_MISMATCH(90117,"SDK版本不匹配"),
    ASF_BASE_DEVICE_MISMATCH(90118,"设备不匹配"),
    ASF_BASE_UNIQUE_IDENTIFIER_MISMATCH(90119,"唯一标识不匹配"),
    ASF_BASE_PARAM_NULL(90120,"参数为空"),
    ASF_BASE_SDK_EXPIRED(90121,"SDK已过期"),
    ASF_BASE_VERSION_NOT_SUPPORT(90122,"版本不支持"),
    ASF_BASE_SIGN_ERROR(90123,"签名错误"),
    ASF_BASE_DATABASE_ERROR(90124,"数据库插入错误"),
    ASF_BASE_UNIQUE_CHECKOUT_FAIL(90125,"唯一标识符校验失败"),
    ASF_BASE_COLOR_SPACE_NOT_SUPPORT(90126,"输入的颜色空间不支持"),
    ASF_BASE_IMAGE_WIDTH_NOT_SUPPORT(90127,"输入图像的byte数据长度不正确"),
    ASF_NETWORK_BASE_COULDNT_RESOLVE_HOST(94209,"无法解析主机地址"),
    ASF_NETWORK_BASE_COULDNT_CONNECT_SERVER(94210,"无法连接服务器"),
    ASF_NETWORK_BASE_CONNECT_TIMEOUT(94211,"网络连接超时"),
    ASF_NETWORK_BASE_UNKNOWN_ERROR(94212,"未知错误");


    private Integer code;
    private String description;

     ErrorCodeEnum(Integer code,String description){
        this.code=code;
        this.description=description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ErrorCodeEnum getDescriptionByCode(Integer code){
        for(ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()){
            if(code.equals(errorCodeEnum.getCode())){
                return errorCodeEnum;
            }
        }
        return  ErrorCodeEnum.UNKNOWN;
    }

}
