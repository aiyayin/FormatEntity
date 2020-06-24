package com.yin.swaggerformat;

import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import java.awt.event.*;

public class SimpleDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea;
    private JTextField textField;

    public SimpleDialog() {
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
        if(textArea !=null){
            String text = textArea.getText().trim().toString();
            Log.error("input text : "+text+"\n :难道是输入的字符串的问题 ");
            String s = ParseUtils.parseString(text);
//
//            targetClass.add(factory.createMethodFromText(s, targetClass));
//
//            // 导入需要的类
//            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
//            styleManager.optimizeImports(file);
//            styleManager.shortenClassReferences(targetClass);
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
