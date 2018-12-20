package com.jeizas.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author jeizas
 * @date 2018-12-19 22:56
 */
public abstract class JSONType<T> {
    final Type type = this.getSuperclassTypeParameter(this.getClass());

    protected JSONType() {
    }

    Type getSuperclassTypeParameter(Class<?> subclass) {
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public final Type getType() {
        return this.type;
    }
}
