package com.heros.follow.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * Created by root on 2017/1/6.
 */
public class Cryptography {
    public static String encodeMD5(String TimeString) {
        String md5String = null;
        String Plaintext = TimeString;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Plaintext.getBytes());
            byte[] digest = md.digest();
            md5String = toHex(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5String;
    }
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

    public static String decodeAES(String key, String input){
        byte[] output = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decodeBase64(input));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new String(output);
    }

    public static String encodeAES(String key, String input){
        byte[] crypted = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(crypted));
    }
}
