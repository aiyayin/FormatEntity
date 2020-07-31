package com.yin.formatentity;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;

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
            ClassParseUtil parseUtil = ClassParseUtil.getInstance(type);
            boolean isJustField = justAttributesCheckBox.isSelected();
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

            PsiFile psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);
            Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);

            if (psiFile == null || editor == null || project == null) {
                return;
            }


            int offset = editor.getCaretModel().getOffset();
            PsiElement element = psiFile.findElementAt(offset);
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
            PsiClass targetClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            for (int i = strings.length - 1; i >= 0; i--) {
                String txt = strings[i];
                List<String> fieldList = new ArrayList<>();
                String name = parseUtil.getClassNameStringAndField(type == YAPI ? yapiEntityNameTextField.getText().trim() : "", txt, fieldList);

                WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                    @Override
                    public void run() {
                        if (isJustField) {
                            insertField(targetClass, psiFile, editor, offset, fieldList);
                        } else {
                            createClassAndField(targetClass, factory, name, fieldList, psiFile);
                        }
                    }
                });

            }

            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }

    private void insertField(PsiElement targetClass, PsiFile psiFile, Editor editor, int offset, List<String> fieldList) {
        Document document = editor.getDocument();
        int lineNumber = document.getLineNumber(offset);
        for (String fieldString : fieldList) {
            int nextLineStartOffset = document.getLineStartOffset(lineNumber + 1);
            document.insertString(nextLineStartOffset, "\t" + fieldString);
            lineNumber++;
        }
        importClass(targetClass, psiFile);
    }

    private void createClassAndField(PsiElement targetClass, PsiJavaParserFacade factory, String name, List<String> fieldList, PsiFile psiFile) {
        if (targetClass != null) {
            PsiClass classFromText = factory.createClassFromText(name, targetClass);
            PsiClass[] classFromTextInnerClasses = classFromText.getInnerClasses();
            if (classFromTextInnerClasses.length > 0) {
                PsiClass innerClass = classFromTextInnerClasses[0];
                if (innerClass != null && fieldList.size() > 0)
                    for (String fieldString : fieldList) {
                        innerClass.add(factory.createFieldFromText(fieldString, innerClass));
                    }
                if (innerClass != null) {
                    targetClass.add(innerClass);
                }
                importClass(targetClass, psiFile);
            }
        }
    }

    private void importClass(PsiElement targetClass, PsiFile psiFile) {
        PsiDocumentManager mPsiDocumentManager = PsiDocumentManager.getInstance(project);
        Document document = mPsiDocumentManager.getCachedDocument(psiFile);
        if (document != null) {
            mPsiDocumentManager.commitDocument(document);
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(psiFile);
        styleManager.shortenClassReferences(targetClass);
    }

}
