package com.yin.formatentity;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleDialog extends JDialog {
    public static int SWAGGER = 1;
    public static int YAPI = 2;
    AnActionEvent anActionEvent;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JComboBox comboBox1;
    private JPanel contentPane;
    private JCheckBox justAttributesCheckBox;
    private Project project;
    private JTextArea textArea;
    private int type = YAPI;
    private JTextField yapiEntityNameTextField;
    private JCheckBox isKotlinCheckBox;

    public SimpleDialog(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        yapiEntityNameTextField.setVisible(type == YAPI);
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object[] selectedObjects = e.getItemSelectable().getSelectedObjects();
                if (selectedObjects.length > 0 && "Yapi".equals(selectedObjects[0].toString())) {
                    type = YAPI;
                } else {
                    type = SWAGGER;
                }
                yapiEntityNameTextField.setVisible(type == YAPI);
            }
        });
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
            boolean isJustField = justAttributesCheckBox.isSelected();
            boolean isKotlin = isKotlinCheckBox.isSelected();
            ClassParseUtil parseUtil = ClassParseUtil.getInstance(type, isKotlin);
            String[] strings;
            if (isJustField) {
                strings = new String[]{text};
            } else {
                strings = parseUtil.getVoNum(text);
            }
            if (strings == null || strings.length <= 0) {
                Messages.showErrorDialog("input string is error or null",
                        "Warn");
                return;
            }

            project = anActionEvent.getData(PlatformDataKeys.PROJECT);

            String className = yapiEntityNameTextField.getText().trim();
            if (isKotlin) {
                CreateKotlinClassHelper createClassHelper = new CreateKotlinClassHelper(project, anActionEvent);
                if (createClassHelper.checkNull()) {
                    return;
                }
                for (int i = strings.length - 1; i >= 0; i--) {
                    String txt = strings[i];
                    List<String> fieldList = new ArrayList<>();
                    String nameStr = parseUtil.getClassNameStringAndField(type == YAPI ? className : "", txt, fieldList);

                    WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                        @Override
                        public void run() {
                            if (isJustField) {
                                createClassHelper.insertField(fieldList);
                            } else {
                                createClassHelper.createClassAndField(nameStr, fieldList);
                            }
                        }
                    });

                }
            } else {
                CreateClassHelper createClassHelper = new CreateClassHelper(project, anActionEvent);
                if (createClassHelper.checkNull()) {
                    return;
                }
                for (int i = strings.length - 1; i >= 0; i--) {
                    String txt = strings[i];
                    List<String> fieldList = new ArrayList<>();
                    String nameStr = parseUtil.getClassNameStringAndField(type == YAPI ? className : "", txt, fieldList);

                    WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                        @Override
                        public void run() {
                            if (isJustField) {
                                createClassHelper.insertField(fieldList);
                            } else {
                                createClassHelper.createClassAndField(className, nameStr, fieldList);
                            }
                        }
                    });

                }
            }


            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }


}
