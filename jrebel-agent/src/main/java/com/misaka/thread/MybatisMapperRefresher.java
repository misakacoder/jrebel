package com.misaka.thread;

import com.misaka.logging.Logger;
import com.misaka.util.FileWatcher;
import com.misaka.util.StringUtil;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class MybatisMapperRefresher implements Runnable {

    private static final String SUFFIX = "Mapper.xml";

    private final Configuration configuration;

    public MybatisMapperRefresher(Object sqlSessionFactory) {
        this.configuration = ((SqlSessionFactory) sqlSessionFactory).getConfiguration();
    }

    @Override
    public void run() {
        FileWatcher watcher = new FileWatcher(SUFFIX, this::refreshMapper);
        watcher.watch();
    }

    @SuppressWarnings("unchecked")
    private void refreshMapper(File classpath, String filepath) {
        String relativePath = StringUtil.trim(filepath, classpath.getAbsolutePath());
        relativePath = StringUtil.trim(relativePath, File.separator);
        relativePath = relativePath.replace(File.separator, "/");
        try (
                InputStream parserStream = new FileInputStream(filepath);
                InputStream builderStream = new FileInputStream(filepath)
        ) {
            Field loadedResourcesField = Configuration.class.getDeclaredField("loadedResources");
            loadedResourcesField.setAccessible(true);
            Set<String> loadedResources = (Set<String>) loadedResourcesField.get(configuration);

            XPathParser xPathParser = new XPathParser(parserStream, true, configuration.getVariables(), new XMLMapperEntityResolver());
            XNode xNode = xPathParser.evalNode("/mapper");
            String namespace = xNode.getStringAttribute("namespace");

            Field knownMappersField = MapperRegistry.class.getDeclaredField("knownMappers");
            knownMappersField.setAccessible(true);
            Map<Class<?>, MapperProxyFactory<?>> knownMappers = (Map<Class<?>, MapperProxyFactory<?>>) knownMappersField.get(configuration.getMapperRegistry());

            loadedResources.remove(relativePath);
            knownMappers.remove(Resources.classForName(namespace));
            configuration.getCacheNames().remove(namespace);

            cleanResultMap(xNode.evalNodes("/mapper/resultMap"), namespace);
            cleanParameterMap(xNode.evalNodes("/mapper/parameterMap"), namespace);
            cleanKeyGenerators(xNode.evalNodes("insert|delete|update|select"), namespace);
            cleanSqlElement(xNode.evalNodes("/mapper/sql"), namespace);

            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(builderStream, configuration, relativePath, configuration.getSqlFragments());
            xmlMapperBuilder.parse();
            Logger.info("mapper reload succeeded: %s", relativePath.replace("/", "."));
        } catch (Exception e) {
            Logger.error("mapper reload failed: %s", relativePath.replace("/", "."), e);
        } finally {
            ErrorContext.instance().reset();
        }
    }

    private void cleanResultMap(List<XNode> xNodeList, String namespace) {
        for (XNode xNode : xNodeList) {
            String id = xNode.getStringAttribute("id", xNode.getValueBasedIdentifier());
            configuration.getResultMapNames().remove(id);
            configuration.getResultMapNames().remove(namespace + "." + id);
            clearResultMap(xNode, namespace);
        }
    }

    private void clearResultMap(XNode xNode, String namespace) {
        for (XNode child : xNode.getChildren()) {
            if ("association".equals(child.getName()) || "collection".equals(child.getName()) || "case".equals(child.getName())) {
                if (child.getStringAttribute("select") == null) {
                    String id = child.getStringAttribute("id", child.getValueBasedIdentifier());
                    configuration.getResultMapNames().remove(id);
                    configuration.getResultMapNames().remove(namespace + "." + id);
                    if (child.getChildren() != null && !child.getChildren().isEmpty()) {
                        clearResultMap(child, namespace);
                    }
                }
            }
        }
    }

    private void cleanParameterMap(List<XNode> xNodeList, String namespace) {
        for (XNode xNode : xNodeList) {
            String id = xNode.getStringAttribute("id");
            configuration.getParameterMaps().remove(namespace + "." + id);
        }
    }

    private void cleanKeyGenerators(List<XNode> xNodeList, String namespace) {
        for (XNode xNode : xNodeList) {
            String id = xNode.getStringAttribute("id");
            configuration.getKeyGeneratorNames().remove(id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
            configuration.getKeyGeneratorNames().remove(namespace + "." + id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
            Collection<MappedStatement> mappedStatements = configuration.getMappedStatements();
            List<MappedStatement> mappedStatementList = new ArrayList<>();
            Iterator<MappedStatement> iterator = mappedStatements.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next instanceof MappedStatement) {
                    MappedStatement mappedStatement = (MappedStatement) next;
                    if (mappedStatement.getId().equals(namespace + "." + id)) {
                        mappedStatementList.add(mappedStatement);
                    }
                }
            }
            mappedStatements.removeAll(mappedStatementList);
        }
    }

    private void cleanSqlElement(List<XNode> xNodeList, String namespace) {
        for (XNode xNode : xNodeList) {
            String id = xNode.getStringAttribute("id");
            configuration.getSqlFragments().remove(id);
            configuration.getSqlFragments().remove(namespace + "." + id);
        }
    }
}
