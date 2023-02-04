package com.misaka.util;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.JavaCommandLine;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;

public class JRebelUtil {

    private static final String AGENT_FILENAME = "jrebel-agent.jar";
    private static final String PLUGIN_ID = "com.misaka.jrebel";
    private static final IdeaPluginDescriptor IDEA_PLUGIN_DESCRIPTOR = PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID));

    public static String getPluginPath() {
        return IDEA_PLUGIN_DESCRIPTOR.getPluginPath().toString();
    }

    public static void addJRebelAgent(RunProfileState state) throws ExecutionException {
        String pluginPath = getPluginPath();
        JavaParameters parameters = ((JavaCommandLine) state).getJavaParameters();
        ParametersList parametersList = parameters.getVMParametersList();
        parametersList.add(String.format("-javaagent:%s/lib/%s", pluginPath, AGENT_FILENAME));
    }
}
