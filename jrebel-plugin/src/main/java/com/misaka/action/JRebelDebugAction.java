package com.misaka.action;

import com.intellij.execution.Executor;
import com.intellij.execution.dashboard.actions.ExecutorAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.misaka.executor.JRebelDebugExecutor;
import com.misaka.ui.Icons;
import org.jetbrains.annotations.NotNull;

public class JRebelDebugAction extends ExecutorAction {

    @Override
    protected Executor getExecutor() {
        return JRebelDebugExecutor.instance();
    }

    @Override
    protected void update(@NotNull AnActionEvent event, boolean running) {
        Presentation presentation = event.getPresentation();
        if (running) {
            presentation.setText("JRebel Rerun in Debug Mode");
            presentation.setDescription("JRebel Rerun selected configuration(s) in Debug Mode");
            presentation.setIcon(Icons.DEBUG_16X16.getIcon());
        } else {
            presentation.setText("JRebel Debug");
            presentation.setDescription("JRebel Debug selected configuration(s)");
            presentation.setIcon(Icons.DEBUG_16X16.getIcon());
        }
    }
}
