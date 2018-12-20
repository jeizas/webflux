package com.jeizas.bean;

import java.util.List;

/**
 * @author jeizas
 * @date 2018-12-19 22:50
 */
public class BaseResult<T> extends BaseBean {
    public static final int CODE_SUCCESS = 1000;
    public static final int CODE_ERROR = 0;
    private Integer state;
    private Integer level;
    private String msg;
    /** @deprecated */
    @Deprecated
    private String errormsg;
    private T data;
    private List<T> list;

    public BaseResult() {
    }

    public BaseResult(Integer state, String msg) {
        this.state = state;
        this.msg = msg;
        this.errormsg = msg;
    }

    public BaseResult(Integer state, Integer level, String msg) {
        this.state = state;
        this.level = level;
        this.msg = msg;
        this.errormsg = msg;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        this.errormsg = msg;
    }

    /** @deprecated */
    @Deprecated
    public String getErrormsg() {
        return this.errormsg;
    }

    /** @deprecated */
    @Deprecated
    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public boolean isSuccess() {
        return this.state != null && this.state == 1000;
    }
}

