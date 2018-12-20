package com.jeizas.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author jeizas
 * @date 2018-12-19 14:24
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    Log logger = LogFactory.getLog(getClass());

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResult handleException(Exception e) {
        logger.error(e.getMessage());
        if (e instanceof ServerException) {
            ErrorCode errorCode = ((ServerException) e).getErrorCode();
            return new DefaultResult(errorCode.getCode(), errorCode.getMessage());
        } else {
            return new DefaultResult(-1, "未知错误");
        }
    }
}
