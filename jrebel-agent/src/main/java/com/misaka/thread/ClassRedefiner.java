package com.misaka.thread;

import com.misaka.logging.Logger;
import com.misaka.util.FileWatcher;
import com.misaka.util.StringUtil;

import java.io.File;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

public class ClassRedefiner implements Runnable {

    private static final String SUFFIX = ".class";

    private final Instrumentation instrumentation;
    private final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    public ClassRedefiner(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void run() {
        FileWatcher watcher = new FileWatcher(SUFFIX, this::redefineClass);
        watcher.watch();
    }

    private void redefineClass(File classpath, String filepath) {
        String className = StringUtil.trim(filepath, classpath.getAbsolutePath());
        className = StringUtil.trim(className, File.separator);
        className = StringUtil.trim(className, SUFFIX);
        className = className.replace(File.separator, ".");
        final String finalClassName = className;
        Threads.executor().execute(() -> {
            try {
                ClassDefinition classDefinition = new ClassDefinition(classLoader.loadClass(finalClassName), getBytecode(finalClassName));
                instrumentation.redefineClasses(classDefinition);
                Logger.info("class reload succeeded: %s", finalClassName);
            } catch (Exception e) {
                Logger.error("class reload failed: %s", finalClassName, e);
            }
        });
    }

    private byte[] getBytecode(String className) throws ClassNotFoundException {
        try (InputStream is = classLoader.getResourceAsStream(getClassRelativePath(className))) {
            if (is != null) {
                int available = is.available();
                byte[] bytecode = new byte[available];
                int length = is.read(bytecode);
                if (length != available) {
                    throw new ClassNotFoundException();
                }
                return bytecode;
            }
        } catch (Exception ignored) {

        }
        throw new ClassNotFoundException();
    }

    private String getClassRelativePath(String name) {
        return String.format("%s.class", name.replace(".", File.separator));
    }
}
