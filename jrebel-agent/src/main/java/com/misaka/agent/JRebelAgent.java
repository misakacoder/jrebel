package com.misaka.agent;

import com.misaka.logging.Logger;
import com.misaka.thread.ClassRedefiner;
import com.misaka.thread.Threads;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.Instrumentation;

public class JRebelAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        Logger.info("JRebel is running");
        Threads.executor().execute(new ClassRedefiner(instrumentation));
        try {
            mybatisMapperListener();
        } catch (Exception ignored) {

        }
    }

    private static void mybatisMapperListener() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("org.springframework.boot.SpringApplication");
        CtMethod ctMethod = ctClass.getMethod("run", "([Ljava/lang/String;)Lorg/springframework/context/ConfigurableApplicationContext;");
        ctMethod.insertAfter("com.misaka.thread.Threads.executor().execute(new com.misaka.thread.MybatisMapperRefresher($_.getBean(org.apache.ibatis.session.SqlSessionFactory.class)));");
        ctClass.toClass();
    }
}