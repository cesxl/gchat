package com.demo.common.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

public class EncodeUtil {

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

//    public static void main(String[] args) {
//        String plain = "cesgroup";
//        byte[] salt = EncodeUtil.decodeHex("d2c6f64a");
//        Hash hash = new SimpleHash("SHA1", plain, salt, 1024);
//        byte[] hashPassword = hash.getBytes();
//        System.out.println(EncodeUtil.encodeHex(hashPassword));
//    }
}
