package com.yychat.diy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DynamicBackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image background;
    private int xPosition = 0; // 图像的初始x位置
    private int floatPosition = 0; // 图像的浮动位置
    private int amplitude = 10; // 浮动的振幅
    private int period = 150; // 浮动周期，单位为毫秒

    public DynamicBackgroundPanel() {
        // 加载背景图像
        background = new ImageIcon("com.yychat.view/image/head.gif").getImage();
        background = background.getScaledInstance(550, 120, Image.SCALE_SMOOTH);
        // 创建一个定时器，每10毫秒调用一次update方法
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 更新图像的位置
                int i=getWidth();
                if(getWidth()==0)i++;
                //xPosition = (xPosition + 1) % i;
                // 更新浮动位置
                floatPosition = (int) (amplitude * Math.sin(0.05* Math.PI * System.currentTimeMillis() / period));
                // 重新绘制面板
                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图像
        //g.drawImage(background, xPosition - getWidth(), getHeight() / 1 - background.getHeight(null) / 1 + floatPosition, this);
        g.drawImage(background, xPosition, getHeight() / 2 - background.getHeight(null) /2 + floatPosition, this);
    }

}