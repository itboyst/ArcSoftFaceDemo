package com.itboyst.face.rpc;


import com.itboyst.face.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: st7251
 * @Date: 2018/11/13 14:23
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{


    /**
     * 自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public Response businessException(BusinessException e) {
        log.error(e.getMessage(), e);
        Response response = new Response();
        response.setCode(e.getErrorCode().getCode());
        response.setMsg(e.getMsgCN());
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        Response response = new Response();
        response.setCode(ErrorCodeEnum.PARAM_ERROR.getCode());
        response.setMsg(e.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.error(e.getMessage(), e);
        Response response = new Response();
        response.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        response.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getDescCN());
        return response;
    }




}
