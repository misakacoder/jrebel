package com.misaka.util;

import com.misaka.logging.Logger;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class FileWatcher {

    private static final String FILE_PROTOCOL = "file";

    private final String suffix;
    private final ClassLoader classLoader;
    private final Map<String, Long> filepath;
    private final BiConsumer<File, String> consumer;

    public FileWatcher(String suffix, BiConsumer<File, String> consumer) {
        Assert.hasText(suffix);
        Assert.notNull(consumer);
        this.suffix = suffix;
        this.classLoader = ClassLoader.getSystemClassLoader();
        this.filepath = new HashMap<>();
        this.consumer = consumer;
    }

    public void watch() {
        while (true) {
            try {
                Enumerations<URL> enumerations = new Enumerations<>(classLoader.getResources(""));
                for (URL url : enumerations) {
                    String protocol = url.getProtocol();
                    if (FILE_PROTOCOL.equals(protocol)) {
                        findModifiedFile(null, createFile(url));
                    }
                }
                TimeUnit.SECONDS.sleep(1L);
            } catch (Exception e) {
                Logger.error("", e);
            }
        }
    }

    private void findModifiedFile(File classpath, File file) {
        if (file.isFile()) {
            String path = file.getAbsolutePath();
            if (path.endsWith(suffix)) {
                long lastModifiedTime = getLastModifiedTime(file);
                Long value = filepath.computeIfAbsent(path, key -> lastModifiedTime);
                if (value != lastModifiedTime) {
                    filepath.put(path, lastModifiedTime);
                    consumer.accept(classpath, path);
                }
            }
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    findModifiedFile(classpath == null ? file : classpath, subFile);
                }
            }
        }
    }

    private File createFile(URL url) {
        try {
            return new File(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private long getLastModifiedTime(File file) {
        long lastModifiedTime = file.lastModified();
        try {
            BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            lastModifiedTime = basicFileAttributes.lastModifiedTime().toMillis();
        } catch (Exception e) {
            Logger.error("", e);
        }
        return lastModifiedTime;
    }
}
