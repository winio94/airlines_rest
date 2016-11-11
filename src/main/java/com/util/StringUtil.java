package com.util;

import org.springframework.util.DigestUtils;

/**
 * Created by Michał on 2016-11-11.
 */
public class StringUtil {
    private StringUtil() {
    }

    public static String md5HashedString(String source) {
        return DigestUtils.md5DigestAsHex(source.getBytes());
    }
}