package com.jeizas.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jeizas
 * @date 2018-12-19 22:59
 */
@Slf4j
public class EnumUtil {

    public EnumUtil() {
    }

    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
        if (c != null && !StringUtil.isEmpty(string)) {
            try {
                return Enum.valueOf(c, string.trim());
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

        return null;
    }
}
