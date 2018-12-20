package com.jeizas.exception;

/**
 * @author jeizas
 * @date 2018-12-19 14:32
 */
public class ServerException extends RuntimeException {

    private ErrorCode errorCode;

    public ServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
