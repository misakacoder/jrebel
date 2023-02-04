package com.misaka.ui;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public enum Icons {

    RUN_13X13("/icons/run_13x13.png"),
    RUN_16X16("/icons/run_16x16.png"),
    DEBUG_13X13("/icons/debug_13x13.png"),
    DEBUG_16X16("/icons/debug_16x16.png");

    private final Icon icon;

    Icons(String imagePath) {
        this.icon = IconLoader.getIcon(imagePath, Icons.class);
    }

    public Icon getIcon() {
        return this.icon;
    }
}
