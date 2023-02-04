package com.misaka.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Threads {

    private static final Executor EXECUTOR = new ThreadPoolExecutor(
            4,
            4,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new BasicThreadFactory()
    );

    public static Executor executor() {
        return EXECUTOR;
    }
}
