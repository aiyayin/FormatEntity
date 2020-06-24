package com.yin.swaggerformat;

import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormTestSwing {

    private JPanel north = new JPanel();

    private JPanel center = new JPanel();

    private JPanel south = new JPanel();
    private JTextArea  nameContent;


    public JPanel initNorth() {

        //定义表单的标题部分，放置到IDEA会话框的顶部位置

        JLabel title = new JLabel("输入内容");
        title.setFont(new Font("微软雅黑", Font.PLAIN, 26)); //字体样式
        title.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
        title.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
        north.add(title);

        return north;
    }

    public JPanel initCenter() {
          nameContent = new JTextArea();
        center.add(nameContent);
        return center;
    }

    public JPanel initSouth() {

        //定义表单的提交按钮，放置到IDEA会话框的底部位置

        JButton submit = new JButton("提交");
        submit.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
        submit.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
        south.add(submit);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameContent!=null){
                    String text = nameContent.getText();
                    Log.error("input text : "+text);
                }
            }
        });

        return south;
    }
}
