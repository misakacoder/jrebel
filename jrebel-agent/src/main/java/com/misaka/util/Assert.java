package com.misaka.util;

public class Assert {

    public static void notNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }
    }

    public static void hasText(String str) {
        if (str.isEmpty()) {
            throw new IllegalArgumentException("str is blank");
        }
    }
}
