package com.yin.formatentity;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class MainAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
//        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
//        if (null == mEditor) {
//            return;
//        }
//        getApplication().invokeLater(new Runnable() {
//            public void run() {
//
//                JBPopupFactory factory = JBPopupFactory.getInstance();
//                factory.createHtmlTextBalloonBuilder("this is  title", null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
//                        .setFadeoutTime(5000)
//                        .createBalloon()
//                        .show(factory.guessBestPopupLocation(mEditor), Balloon.Position.below);
//            }
//        });

        SimpleDialog simpleDialog = new SimpleDialog(e);
        simpleDialog.pack();
        simpleDialog.setSize(800, 600);
        simpleDialog.setAlwaysOnTop(false);
        simpleDialog.setLocationRelativeTo(null);
        simpleDialog.setVisible(true);
    }
}
