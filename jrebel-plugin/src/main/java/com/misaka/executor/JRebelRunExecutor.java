package com.misaka.executor;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.openapi.util.NlsActions;
import com.misaka.ui.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class JRebelRunExecutor extends Executor {

    public static String ID = "JRebel Run";

    @Override
    public @NotNull String getToolWindowId() {
        return ID;
    }

    @Override
    public @NotNull Icon getToolWindowIcon() {
        return Icons.RUN_13X13.getIcon();
    }

    @Override
    public @NotNull Icon getIcon() {
        return Icons.RUN_16X16.getIcon();
    }

    @Override
    public Icon getDisabledIcon() {
        return null;
    }

    @Override
    public @NlsActions.ActionDescription String getDescription() {
        return "Run selected configuration with JRebel";
    }

    @Override
    public @NotNull @NlsActions.ActionText String getActionName() {
        return ID;
    }

    @Override
    public @NotNull @NonNls String getId() {
        return ID;
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getStartActionText() {
        return "JRebel Run";
    }

    @Override
    public @NonNls String getContextActionId() {
        return "JRebelRunClass";
    }

    @Override
    public @NonNls String getHelpId() {
        return "ideaInterface.run";
    }

    public static Executor instance() {
        return ExecutorRegistry.getInstance().getExecutorById(ID);
    }
}
