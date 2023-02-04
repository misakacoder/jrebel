package com.misaka.util;

public class StringUtil {

    public static String trim(String str, String preOrSuffix) {
        if (str != null && preOrSuffix != null) {
            if (str.startsWith(preOrSuffix)) {
                str = str.substring(str.indexOf(preOrSuffix) + preOrSuffix.length());
            }
            if (str.endsWith(preOrSuffix)) {
                str = str.substring(0, str.lastIndexOf(preOrSuffix));
            }
        }
        return str;
    }
}
