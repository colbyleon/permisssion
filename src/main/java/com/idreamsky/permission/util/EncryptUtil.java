package com.idreamsky.permission.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: colby
 * @Date: 2018/12/19 22:56
 */
@Slf4j
public class EncryptUtil {
    public static String md5HexString(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(str.getBytes());
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder(bytes.length);
            for (byte aByte : bytes) {
                sb.append(String.format("%02x", aByte));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("md5 error str:{}",str,e);
            throw new RuntimeException("md5错误",e);
        }
    }
}
