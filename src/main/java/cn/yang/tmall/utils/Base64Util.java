package cn.yang.tmall.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author Yangtz
 * @ClassName: Base64Util
 * @Description: jdk1.8自带加密
 * @create 2020-02-23 21:13
 */
public class Base64Util {
    /**
     * @Description: 对字符串加密
     * @Param str 字符串
     * @Author: Yangtz
     * @Date: 2020-02-23 21:14
     */
    public static String encode(String str) {
        try {
            byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
            return new String(encodeBytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 对字节数组加密
     * @Param str 字节数组
     * @Author: Yangtz
     * @Date: 2020-02-23 21:15
     */
    public static String encode(byte[] str) {
        byte[] encodeBytes = Base64.getEncoder().encode(str);
        return new String(encodeBytes);
    }

    /**
     * @Description: 对字符串解密
     * @Param str
     * @Author: Yangtz
     * @Date: 2020-02-23 21:16
     */
    public static String decode(String str) {
        try {
            byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
            return new String(decodeBytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 抛出异常的加密
     * @Param str
     * @Author: Yangtz
     * @Date: 2020-02-23 21:17
     */
    public static String encodeThrowsException(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    /**
     * @Description: 抛出异常的解密
     * @Param str
     * @Author: Yangtz
     * @Date: 2020-02-23 21:18
     */
    public static String decodeThrowsException(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }
}
