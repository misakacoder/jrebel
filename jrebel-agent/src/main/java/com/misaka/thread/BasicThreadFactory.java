package com.misaka.thread;

import java.util.concurrent.ThreadFactory;

public class BasicThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    }
}
