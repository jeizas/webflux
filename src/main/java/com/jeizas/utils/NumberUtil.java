package com.jeizas.utils;

/**
 * @author jeizas
 * @date 2018-12-19 22:57
 */
public class NumberUtil {
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final byte[] VALUES = new byte[128];
    private static final int MIN_RADIX = 2;
    private static final int MAX_RADIX = 62;

    public NumberUtil() {
    }

    public static boolean isValidId(Long id) {
        return id != null && id > 0L;
    }

    public static boolean isInValidId(Long id) {
        return id == null || id <= 0L;
    }

    public static boolean isValidId(Integer id) {
        return id != null && id > 0;
    }

    public static boolean isInValidId(Integer id) {
        return id == null || id <= 0;
    }

    public static boolean isPositive(Integer number) {
        return number != null && number > 0;
    }

    public static boolean isNegative(Integer number) {
        return number != null && number < 0;
    }

    public static boolean isPositive(Long number) {
        return number != null && number > 0L;
    }

    public static boolean isNegative(Long number) {
        return number != null && number < 0L;
    }

    public static int getRandomInTen() {
        return (int)(Math.random() * 10.0D);
    }

    public static int getRandomInValue(int value) {
        return (int)(Math.random() * (double)value);
    }

    public static Long getLong(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        } else {
            try {
                return Long.valueOf(value);
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static long getLong(Long value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Long.valueOf(value);
            } catch (Exception var4) {
                return defaultValue;
            }
        }
    }

    public static Long getLongFromObj(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return obj instanceof Number ? ((Number)obj).longValue() : getLong(obj.toString());
        }
    }

    public static long getLong(String value, long defaultValue) {
        Double doubleValue = getDouble(value);
        long result = defaultValue;
        if (doubleValue != null) {
            result = doubleValue.longValue();
        }

        return result;
    }

    public static int getInteger(String value, int defaultValue) {
        Double doubleValue = getDouble(value);
        int result = defaultValue;
        if (doubleValue != null) {
            result = doubleValue.intValue();
        }

        return result;
    }

    public static Integer getIntegerFromObj(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return obj instanceof Number ? ((Number)obj).intValue() : getInteger(obj.toString());
        }
    }

    public static Integer getInteger(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        } else {
            try {
                return Integer.valueOf(value);
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static Double getDouble(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        } else {
            try {
                return Double.valueOf(value);
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static String to62RadixString(long i) {
        return toRadixString(i, 62);
    }

    public static String toRadixString(long i, int radix) {
        if (radix < 2 || radix > 62) {
            radix = 10;
        }

        if (radix == 10) {
            return Long.toString(i);
        } else {
            char[] buf = new char[65];
            int charPos = 64;
            boolean negative = i < 0L;
            if (!negative) {
                i = -i;
            }

            while(i <= (long)(-radix)) {
                buf[charPos--] = DIGITS[(int)(-(i % (long)radix))];
                i /= (long)radix;
            }

            buf[charPos] = DIGITS[(int)(-i)];
            if (negative) {
                --charPos;
                buf[charPos] = '-';
            }

            return new String(buf, charPos, 65 - charPos);
        }
    }

    public static long radixValue2Long(String value, int radix) {
        return parseLong(value, radix);
    }

    public static int radixValue2Int(String value, int radix) {
        return (int)radixValue2Long(value, radix);
    }

    public static int radixValue2SafeInt(String value, int radix) {
        long result = radixValue2Long(value, radix);
        if (result <= 2147483647L && result >= -2147483648L) {
            return (int)result;
        } else {
            throw forInputString(value);
        }
    }

    public static long parseLong(String value, int radix) throws NumberFormatException {
        if (value == null) {
            throw new NumberFormatException("null");
        } else if (radix < 2) {
            throw new NumberFormatException("radix " + radix + " less than MIN_RADIX");
        } else if (radix > 62) {
            throw new NumberFormatException("radix " + radix + " greater than MAX_RADIX");
        } else {
            long result = 0L;
            boolean negative = false;
            int i = 0;
            int len = value.length();
            long limit = -9223372036854775807L;
            if (len > 0) {
                char firstChar = value.charAt(0);
                if (firstChar < '0') {
                    if (firstChar == '-') {
                        negative = true;
                        limit = -9223372036854775808L;
                    } else if (firstChar != '+') {
                        throw forInputString(value);
                    }

                    if (len == 1) {
                        throw forInputString(value);
                    }

                    ++i;
                }

                int digit;
                for(long multmin = limit / (long)radix; i < len; result -= (long)digit) {
                    if (radix > 36) {
                        digit = VALUES[value.charAt(i++)];
                    } else {
                        digit = Character.digit(value.charAt(i++), radix);
                    }

                    if (digit < 0) {
                        throw forInputString(value);
                    }

                    if (result < multmin) {
                        throw forInputString(value);
                    }

                    result *= (long)radix;
                    if (result < limit + (long)digit) {
                        throw forInputString(value);
                    }
                }

                return negative ? result : -result;
            } else {
                throw forInputString(value);
            }
        }
    }

    private static NumberFormatException forInputString(String s) {
        return new NumberFormatException("For input string: \"" + s + "\"");
    }

    private static long parseLarger36(String value, int radix) {
        int len = value.length();
        long result = 0L;

        for(int i = 0; i < len; ++i) {
            result += (long)VALUES[value.charAt(i)] * (long)Math.pow((double)radix, (double)(len - 1 - i));
        }

        return result;
    }

    public static String random() {
        String str = "";
        str = str + (int)(Math.random() * 9.0D + 1.0D);

        for(int i = 0; i < 3; ++i) {
            str = str + (int)(Math.random() * 10.0D);
        }

        return str;
    }

    public static boolean checkNum(String value) {
        return !StringUtil.isEmpty(value) && value.matches("\\d+");
    }

    public static boolean equals(Integer v1, Integer v2) {
        return v1 != null && v2 != null && v1.equals(v2);
    }

    public static boolean equals(Long v1, Long v2) {
        return v1 != null && v2 != null && v1.equals(v2);
    }

    public static boolean equals(Long v1, Integer v2) {
        return v1 != null && v2 != null && v1.equals(v2.longValue());
    }

    public static Long delta(Long v1, Long v2) {
        return v1 != null && v2 != null ? v1 - v2 : null;
    }

    public static Integer delta(Integer v1, Integer v2) {
        return v1 != null && v2 != null ? v1 - v2 : null;
    }

    public static Long delta(Long v1, Integer v2) {
        return v1 != null && v2 != null ? v1 - (long)v2 : null;
    }

    static {
        for(int i = 0; i < 128; ++i) {
            VALUES[i] = 0;
        }

        int value = 0;

        for(int i = 0; i < DIGITS.length; ++i) {
            VALUES[DIGITS[i]] = (byte)i;
        }

    }
}

