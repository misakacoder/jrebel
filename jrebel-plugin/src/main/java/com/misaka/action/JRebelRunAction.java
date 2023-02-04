package com.misaka.action;

import com.intellij.execution.Executor;
import com.intellij.execution.dashboard.actions.ExecutorAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.misaka.executor.JRebelRunExecutor;
import com.misaka.ui.Icons;
import org.jetbrains.annotations.NotNull;

public class JRebelRunAction extends ExecutorAction {

    @Override
    protected Executor getExecutor() {
        return JRebelRunExecutor.instance();
    }

    @Override
    protected void update(@NotNull AnActionEvent event, boolean running) {
        Presentation presentation = event.getPresentation();
        if (running) {
            presentation.setText("JRebel Rerun");
            presentation.setDescription("JRebel Rerun selected configuration(s)");
            presentation.setIcon(Icons.RUN_16X16.getIcon());
        } else {
            presentation.setText("JRebel Run");
            presentation.setDescription("JRebel Run selected configuration(s)");
            presentation.setIcon(Icons.RUN_16X16.getIcon());
        }
    }
}
