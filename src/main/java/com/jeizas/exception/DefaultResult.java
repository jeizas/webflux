package com.jeizas.exception;

/**
 * @author jeizas
 * @date 2018-12-19 15:22
 */
public class DefaultResult {

    private int code;
    private String message;

    public DefaultResult(String message) {
        this.message = message;
    }

    public DefaultResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
