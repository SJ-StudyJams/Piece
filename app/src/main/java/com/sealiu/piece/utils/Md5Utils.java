package com.sealiu.piece.utils;

import java.security.MessageDigest;

/**
 * Created by vince on 2016-07-04.
 */
public class Md5Utils {
    /**
     * @param str 需要加密的字符串
     * @return 字符加密后的Md5值
     */
    public static String encode(String str) {
        String res = "";
        String s = "";
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            //MD5 加密后的字节数组
            byte[] digest = md.digest(str.getBytes());
            //把字节数组转换成字符串
            for (byte b : digest) {
                //把一个字节转换成16进制
                //去掉一个int类型前3个字节 0a
                int d = b & 0x000000ff;
                s = Integer.toHexString(d);
                if (s.length() == 1) {
                    s = "0" + s;
                }
                res = res + s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
