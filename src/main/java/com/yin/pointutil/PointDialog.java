package com.yin.pointutil;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.yin.formatentity.CreateClassHelper;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PointDialog extends JDialog {
    AnActionEvent anActionEvent;
    private JButton buttonCancel;
    private JButton buttonOK;

    private JPanel contentPane;
    private Project project;
    private JTextArea textArea;

    public PointDialog(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        if (textArea != null) {
            String text = textArea.getText().trim().toString();
            PointParseUtil parseUtil = new PointParseUtil();


            if (text.length() <= 0) {
                Messages.showErrorDialog("input string is error or null",
                        "Warn");
                return;
            }

            project = anActionEvent.getData(PlatformDataKeys.PROJECT);

            CreateClassHelper createClassHelper = new CreateClassHelper(project, anActionEvent);
            if (createClassHelper.checkNull()) {
                return;
            }


            String string = parseUtil.getClassNameStringAndField(text);
            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    createClassHelper.insertString(string);
                }
            });

            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }


}
