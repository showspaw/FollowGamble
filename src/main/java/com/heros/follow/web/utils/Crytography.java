package com.heros.follow.web.utils;

import java.security.MessageDigest;

/**
 * Created by root on 2017/1/6.
 */
public class Crytography {
    public static String encodeSHA1(String plainText) {
            String sha1String = null;
            try {
                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                sha1.update(plainText.getBytes());
                byte[] digest = sha1.digest();
                sha1String = toHex(digest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sha1String;
    }
    private static String toHex(byte[] digest) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < digest.length; ++i) {
            byte b = digest[i];
            int value = (b & 0x7F) + (b < 0 ? 128 : 0);
            buffer.append(value < 16 ? "0" : "");
            buffer.append(Integer.toHexString(value));
        }
        return buffer.toString();
    }
}
