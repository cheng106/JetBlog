package com.cheng.jetblog.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author cheng
 * @since 2021/8/19 00:38
 **/
public class EncryptStr {

    public static String encryptShortSHE256(String s) {
        return DigestUtils.md5Hex(DigestUtils.sha256Hex(s));
    }
}
