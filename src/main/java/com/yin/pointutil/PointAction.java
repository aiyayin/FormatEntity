package com.yin.pointutil;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class PointAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PointDialog simpleDialog = new PointDialog(e);
        simpleDialog.pack();
        simpleDialog.setSize(800, 600);
        simpleDialog.setAlwaysOnTop(false);
        simpleDialog.setLocationRelativeTo(null);
        simpleDialog.setVisible(true);
    }
}
