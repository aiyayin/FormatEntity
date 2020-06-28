package com.yin.swaggerformat;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

import static android.util.Log.e;

public class SimpleDialog extends JDialog {
    AnActionEvent anActionEvent;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JPanel contentPane;
    private Project project;
    private PsiClass psiClass;
    private JTextArea textArea;

    public SimpleDialog(AnActionEvent anActionEvent) {
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
            String[] strings = ClassParseUtils.getVoNum(text);
            if (strings == null || strings.length <= 0) {
                e("SimpleDialog>", "input string is error or null");
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

            PsiClass targetClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            for (String txt: strings) {
                String name = ClassParseUtils.getClassNameString(txt);
                List<String> fieldList = ClassParseUtils.parseString(txt);
                WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                    @Override
                    public void run() {
                        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
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
                                JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
                                styleManager.optimizeImports(psiFile);
                                styleManager.shortenClassReferences(targetClass);
                            }
                        }
                    }
                });

            }

            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
//    public static void main(String[] args) {
//        SimpleDialog dialog = new SimpleDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }

}
