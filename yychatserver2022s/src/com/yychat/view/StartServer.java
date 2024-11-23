package com.yychat.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/*实验11_启动服务器并监听端口，第5步：导入YychatServer类*/
import com.yychat.control.YychatServer;
/*实验11_启动服务器并监听端口，第1步：实现ActionListener接口*/
public class StartServer extends JFrame implements ActionListener{
	JButton jb1,jb2;
	public StartServer(){
		jb1=new JButton("启动服务器");
		/*实验11_启动服务器并监听端口，第3步：再启动按钮上添加动作监听器*/
		jb1.addActionListener(this);
		jb1.setFont(new Font("宋体",Font.BOLD,25));//设置字体
		jb2=new JButton("停止服务器");
		jb2.setFont(new Font("宋体",Font.BOLD,25));

		this.setLayout(new GridLayout(1,2));//网格布局
		this.add(jb1);
		this.add(jb2);
		this.setSize(400,100);
		this.setLocationRelativeTo(null);
		this.setIconImage(new ImageIcon("com.yychat.view/image/duck2.gif").getImage());
		this.setTitle("YYchat服务器");
		this.setVisible(true);
	}

	public static void main(String[] args) {
		StartServer ss=new StartServer();

	}
	/*实验11_启动服务器并监听端口，第2步：实现actionPerformed方法*/
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource()==jb1)
			new YychatServer();//创建yychat服务器对象
	}
}