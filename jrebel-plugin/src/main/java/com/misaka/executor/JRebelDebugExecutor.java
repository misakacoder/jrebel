package com.misaka.executor;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.openapi.util.NlsActions;
import com.misaka.ui.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class JRebelDebugExecutor extends Executor {

    public static String ID = "JRebel Debug";

    @Override
    public @NotNull String getToolWindowId() {
        return ID;
    }

    @Override
    public @NotNull Icon getToolWindowIcon() {
        return Icons.DEBUG_13X13.getIcon();
    }

    @Override
    public @NotNull Icon getIcon() {
        return Icons.DEBUG_16X16.getIcon();
    }

    @Override
    public Icon getDisabledIcon() {
        return null;
    }

    @Override
    public @NlsActions.ActionDescription String getDescription() {
        return "Debug selected configuration with JRebel";
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
        return "JRebel Debug";
    }

    @Override
    public @NonNls String getContextActionId() {
        return "JRebelDebugClass";
    }

    @Override
    public @NonNls String getHelpId() {
        return "debugging.DebugWindow";
    }

    public static Executor instance() {
        return ExecutorRegistry.getInstance().getExecutorById(ID);
    }
}
