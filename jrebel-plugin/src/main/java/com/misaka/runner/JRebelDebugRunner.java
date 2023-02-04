package com.misaka.runner;

import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction;
import com.intellij.execution.target.TargetEnvironmentAwareRunProfileState;
import com.intellij.execution.ui.RunContentDescriptor;
import com.misaka.executor.JRebelDebugExecutor;
import com.misaka.util.JRebelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;

public class JRebelDebugRunner extends GenericDebuggerRunner {

    private static final String ID = JRebelDebugExecutor.ID;

    @Override
    public @NotNull String getRunnerId() {
        return ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        boolean isJRebelRunExecutor = ID.equals(executorId);
        boolean isModuleRunProfile = profile instanceof ModuleRunProfile;
        boolean isRunConfigurationWithSuppressedDefaultRunAction = profile instanceof RunConfigurationWithSuppressedDefaultRunAction;
        return isJRebelRunExecutor && isModuleRunProfile && !isRunConfigurationWithSuppressedDefaultRunAction;
    }

    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {
        JRebelUtil.addJRebelAgent(state);
        return super.doExecute(state, env);
    }

    @Override
    protected @NotNull Promise<@Nullable RunContentDescriptor> doExecuteAsync(@NotNull TargetEnvironmentAwareRunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {
        JRebelUtil.addJRebelAgent(state);
        return super.doExecuteAsync(state, env);
    }
}
