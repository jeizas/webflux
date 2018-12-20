package com.jeizas.utils;

/**
 * @author jeizas
 * @date 2018-12-19 22:56
 */
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringUtil {
    public static final String SIMPLE_PHONE = "^[1][0-9]{10}$";
    public static final String EMAIL = "^\\w+([-_.]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,6})+$";
    private static final char[] CHINESE_NUM = new char[]{'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    private static final char[] CHINESE_UNIT = new char[]{'里', '分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万', '拾', '佰', '仟'};

    public StringUtil() {
    }

    public static String transNull(String str) {
        return str != null ? str : "";
    }

    public static String[] split(String line, String delim) {
        if (line == null) {
            return new String[0];
        } else {
            List<String> list = new ArrayList();
            StringTokenizer t = new StringTokenizer(line, delim);

            while(t.hasMoreTokens()) {
                list.add(t.nextToken());
            }

            return (String[])list.toArray(new String[list.size()]);
        }
    }

    public static String transChiTo8859(String chi) {
        if (isEmpty(chi)) {
            return "";
        } else {
            String result = null;

            try {
                byte[] temp = chi.getBytes("GBK");
                result = new String(temp, "ISO-8859-1");
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }

            return result;
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static boolean equalsIgnoreCase(String v1, String v2) {
        return v1 != null && v2 != null ? v1.equalsIgnoreCase(v2) : false;
    }

    public static boolean equals(String v1, String v2) {
        return v1 != null && v2 != null ? v1.equals(v2) : false;
    }

    public static boolean isTrue(Boolean value) {
        return value != null && value;
    }

    public static boolean isTrue(String value) {
        return isTrue(Boolean.valueOf(value));
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isNull(Object obj) {
        if (obj instanceof String) {
            return isEmpty((String)obj);
        } else {
            return obj == null;
        }
    }

    public static void checkNull(Object obj) {
        if (isNull(obj)) {
            throw new IllegalArgumentException();
        }
    }

    public static String formatContent(String content, Object... args) {
        return args != null && args.length > 0 ? MessageFormat.format(content, args) : content;
    }

    public static String toRMB(int moneyNum) {
        String res = "";
        int i = 3;
        if (moneyNum == 0) {
            return "零元";
        } else {
            while(moneyNum > 0) {
                res = CHINESE_UNIT[i++] + res;
                res = CHINESE_NUM[moneyNum % 10] + res;
                moneyNum /= 10;
            }

            return res.replaceAll("零[拾佰仟]", "零").replaceAll("零+亿", "亿").replaceAll("零+万", "万").replaceAll("零+元", "元").replaceAll("零+", "零");
        }
    }

    public static String toRMB(String price) throws IllegalArgumentException {
        String res = "";
        int i = 3;
        int len = price.length();
        if (len > CHINESE_UNIT.length - 3) {
            throw new IllegalArgumentException("price too large!");
        } else if ("0".equals(price)) {
            return "零元";
        } else {
            --len;

            while(len >= 0) {
                res = CHINESE_UNIT[i++] + res;
                int num = Integer.parseInt(price.charAt(len) + "");
                res = CHINESE_NUM[num] + res;
                --len;
            }

            return res.replaceAll("零[拾佰仟]", "零").replaceAll("零+亿", "亿").replaceAll("零+万", "万").replaceAll("零+元", "元").replaceAll("零+", "零");
        }
    }

    public static String toRMB(double price) {
        String res = "";
        String money = String.format("%.3f", price);
        int i = 0;
        if (price == 0.0D) {
            return "零元";
        } else {
            String inte = money.split("\\.")[0];

            for(int deci = Integer.parseInt(money.split("\\.")[1].substring(0, 3)); deci > 0; deci /= 10) {
                res = CHINESE_UNIT[i++] + res;
                res = CHINESE_NUM[deci % 10] + res;
            }

            res = res.replaceAll("零[里分角]", "零");
            if (i < 3) {
                res = "零" + res;
            }

            res = res.replaceAll("零+", "零");
            if (res.endsWith("零")) {
                res = res.substring(0, res.length() - 1);
            }

            return toRMB(inte) + res;
        }
    }

    public static boolean isChinese(char c) {
        UnicodeBlock ub = UnicodeBlock.of(c);
        return ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.GENERAL_PUNCTUATION || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isMobile(String mobile) {
        if (isEmpty(mobile)) {
            return false;
        } else if (!"".equals(mobile) && mobile.contains("-")) {
            return true;
        } else {
            Pattern p = Pattern.compile("^[1][0-9]{10}$");
            Matcher m = p.matcher(mobile);
            return m.matches();
        }
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^\\w+([-_.]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,6})+$");
            Matcher mat = pattern.matcher(email);

            boolean tag;
            try {
                tag = mat.matches();
            } catch (Exception var5) {
                tag = false;
            }

            return tag;
        }
    }

    public static String getRandomNumber(int len, boolean withZero) {
        StringBuilder builder = new StringBuilder();

        while(builder.length() < len) {
            int number = (int)(Math.random() * 10.0D);
            if (withZero) {
                builder.append(number);
            } else if (number > 0) {
                builder.append(number);
            }
        }

        return builder.toString();
    }

    public static String getRandomLetter(int len) {
        String str = "";

        for(int i = 0; i < len; ++i) {
            str = str + NumberUtil.to62RadixString((long)((int)(Math.random() * 63.0D)));
        }

        return str;
    }

    public static String getRandomLetterDownCase(int len) {
        String str = "";

        for(int i = 0; i < len; ++i) {
            str = str + (char)((int)(Math.random() * 26.0D + 97.0D));
        }

        return str;
    }

    public static String getRandomLetterUpCase(int len) {
        String str = "";

        for(int i = 0; i < len; ++i) {
            str = str + (char)((int)(Math.random() * 26.0D + 65.0D));
        }

        return str;
    }

    public static String safeHtml(String html) {
        String result = html;
        List<String> unSafeLables = new ArrayList();
        unSafeLables.add("<iframe[^>]*?>.*?</iframe>");
        unSafeLables.add("<frame[^>]*?>.*?</frame>");
        unSafeLables.add("<script[^>]*?>.*?</script>");
        unSafeLables.add("<title[^>]*?>.*?</title>");
        unSafeLables.add("<meta[^>]*?>");
        unSafeLables.add("<noscript[^>]*?>");
        unSafeLables.add("href=\"javascript:.*?\"");

        String lable;
        for(Iterator var3 = unSafeLables.iterator(); var3.hasNext(); result = result.replaceAll(lable, "")) {
            lable = (String)var3.next();
        }

        return result;
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        } else if (cs1 != null && cs2 != null) {
            return cs1 instanceof String && cs2 instanceof String ? cs1.equals(cs2) : regionMatches(cs1, false, 0, cs2, 0, Math.max(cs1.length(), cs2.length()));
        } else {
            return false;
        }
    }

    private static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length);
        } else {
            int index1 = thisStart;
            int index2 = start;
            int var8 = length;

            while(var8-- > 0) {
                char c1 = cs.charAt(index1++);
                char c2 = substring.charAt(index2++);
                if (c1 != c2) {
                    if (!ignoreCase) {
                        return false;
                    }

                    if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    private static byte[] getBytes(String string, Charset charset) {
        return string == null ? null : string.getBytes(charset);
    }

    public static byte[] getBytesIso8859_1(String string) {
        return getBytes(string, StandardCharsets.ISO_8859_1);
    }

    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        } else {
            try {
                return string.getBytes(charsetName);
            } catch (UnsupportedEncodingException var3) {
                throw newIllegalStateException(charsetName, var3);
            }
        }
    }

    public static byte[] getBytesUsAscii(String string) {
        return getBytes(string, StandardCharsets.US_ASCII);
    }

    public static byte[] getBytesUtf16(String string) {
        return getBytes(string, StandardCharsets.UTF_16);
    }

    public static byte[] getBytesUtf16Be(String string) {
        return getBytes(string, StandardCharsets.UTF_16BE);
    }

    public static byte[] getBytesUtf16Le(String string) {
        return getBytes(string, StandardCharsets.UTF_16LE);
    }

    public static byte[] getBytesUtf8(String string) {
        return getBytes(string, StandardCharsets.UTF_8);
    }

    private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }

    private static String newString(byte[] bytes, Charset charset) {
        return bytes == null ? null : new String(bytes, charset);
    }

    public static String newString(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException var3) {
                throw newIllegalStateException(charsetName, var3);
            }
        }
    }

    public static String newStringIso8859_1(byte[] bytes) {
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    public static String newStringUsAscii(byte[] bytes) {
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public static String newStringUtf16(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_16);
    }

    public static String newStringUtf16Be(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_16BE);
    }

    public static String newStringUtf16Le(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_16LE);
    }

    public static String newStringUtf8(byte[] bytes) {
        return newString(bytes, StandardCharsets.UTF_8);
    }

    public static String notBlank(String value, String defaultValue) {
        return isEmpty(value) ? defaultValue : value;
    }

    public static String hiddenPhone(String phone) {
        return hiddenPhone(phone, 4);
    }

    public static String hiddenPhone(String phone, int count) {
        return phone == null ? phone : hiddenMiddle(phone, count);
    }

    public static String hiddenLeft(String orgStr, int count) {
        return hidden(orgStr, 0, count);
    }

    public static String hiddenRight(String orgStr, int count) {
        return orgStr == null ? orgStr : hidden(orgStr, orgStr.length() - count, orgStr.length());
    }

    public static String hiddenMiddle(String orgStr, int count) {
        if (orgStr == null) {
            return orgStr;
        } else {
            int len = orgStr.length();
            if (len < count + 2) {
                return orgStr;
            } else {
                int start = (len - count) / 2;
                int end = start + count;
                return hidden(orgStr, start, end);
            }
        }
    }

    public static String hidden(String orgStr, int start, int end) {
        if (orgStr == null) {
            return orgStr;
        } else {
            int len = orgStr.length();
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < len; ++i) {
                if (i >= start && i < end) {
                    sb.append("*");
                } else {
                    sb.append(orgStr.charAt(i));
                }
            }

            return sb.toString();
        }
    }

    public static boolean checkEmoji(String source) {
        String regEx = "[^\\u0000-\\uFFFF]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        return m.find();
    }

    public static String filterEmoji(String source) {
        source = source.replaceAll("[^\\u0000-\\uFFFF]", "");
        return source;
    }

    public static boolean checkSymbol(String source) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        return m.find();
    }

    public static String filterSymbol(String source) {
        if (isEmpty(source)) {
            return "";
        } else {
            source = source.replaceAll(" ", "");
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(source);
            return m.replaceAll("").trim();
        }
    }
}

