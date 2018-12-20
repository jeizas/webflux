package com.jeizas.bean;

import com.jeizas.utils.JSONUtil;

/**
 * @author jeizas
 * @date 2018-12-19 22:51
 */
public class BaseBean {

    public BaseBean() {
    }

    @Override
    public final String toString() {
        return JSONUtil.obj2Json(this);
    }
}
