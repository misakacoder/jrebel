package com.misaka.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Logger {

    private static final String LOG_FORMAT = "\u001B[%sm%s - %s\u001B[0m\n";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static void info(String message) {
        println("32", message);
    }

    public static void info(String format, Object... args) {
        info(String.format(format, args));
    }

    public static void error(String message) {
        println("31", message);
    }

    public static void error(String format, Object... args) {
        List<Object> objectList = new ArrayList<>();
        List<Exception> exceptionList = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof Exception) {
                exceptionList.add((Exception) arg);
            } else {
                objectList.add(arg);
            }
        }
        error(String.format(format, objectList.toArray()));
        for (Exception exception : exceptionList) {
            exception.printStackTrace();
        }
    }

    private static void println(String color, String message) {
        System.out.printf(LOG_FORMAT, color, LocalDateTime.now().format(DATE_TIME_FORMATTER), message);
    }
}
