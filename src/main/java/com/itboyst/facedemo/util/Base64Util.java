package com.itboyst.facedemo.util;

import org.springframework.util.ObjectUtils;
import java.util.Base64;

/**
 * @author st7251
 * @date 2020/3/13 9:23
 */
public class Base64Util {
    public static String base64Process(String base64Str) {
        if (!ObjectUtils.isEmpty(base64Str)) {
            String photoBase64 = base64Str.substring(0, 30).toLowerCase();
            int indexOf = photoBase64.indexOf("base64,");
            if (indexOf > 0) {
                base64Str = base64Str.substring(indexOf + 7);
            }
            base64Str = base64Str.replaceAll(" ", "+");
            base64Str = base64Str.replaceAll("\r|\n", "");
            return base64Str;
        }
        return "";
    }

    public static byte[] base64ToBytes(String base64) {
        if (ObjectUtils.isEmpty(base64)) {
            return null;
        }
        String base64Process = base64Process(base64);

        byte[] decode = Base64.getDecoder().decode(base64Process);
        return decode;


    }
}
