package com.yychat.view;

import com.yychat.control.YychatClientConnection;
import com.yychat.diy.DynamicBackgroundPanel;
import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.model.User;
import com.yychat.model.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;

/**
 * 客户端
 */

public class ClientLogin extends JFrame implements ActionListener{
	public static HashMap hmFriendList=new HashMap<String,FriendList>();
	JLabel jl,jll;//定义标签组件
	JButton jb1,jb2,jb3;
	JPanel jp;

	//定义登陆界面中间部分的组件
	JLabel jl1,jl2,jl3,jl4,jl0,jl01,jl02;
	JTextField jtf;
	JPasswordField jpf;
	JButton jb4;
	JCheckBox jc1,jc2;
	JPanel jp1,jp2,jp3;
	JTabbedPane jtp;

	public ClientLogin(){

		DynamicBackgroundPanel dynamicBackgroundPanel =new DynamicBackgroundPanel();
		dynamicBackgroundPanel.setPreferredSize(new Dimension(200, 105));
		this.add(dynamicBackgroundPanel,"North");

		jll=new JLabel(new ImageIcon("com.yychat.view/image/image9.jpg"));
		jll.setPreferredSize(new Dimension(50,50));

		//创建登录界面中间部分的组件
		jl0=new JLabel("",JLabel.CENTER);
		jl02=new JLabel("",JLabel.CENTER);
		jl1=new JLabel("    YY号码:",JLabel.CENTER);
		jl2=new JLabel("    YY密码:",JLabel.CENTER);
		jl3=new JLabel("    忘记密码",JLabel.CENTER);
		jl3.setForeground(Color.blue);//设置字体蓝色
		jl4=new JLabel("申请密码保护",JLabel.CENTER);
		jb4=new JButton(new ImageIcon("com.yychat.view/image/clear.gif"));
		jtf=new JTextField();
		jpf=new JPasswordField();
		jc1=new JCheckBox("隐身登录");
		jc2=new JCheckBox("记住密码");
		jp1=new JPanel(new GridLayout(4,3));//设置网格布局模式，默认是FlowLayout布局

		jp1.add(jl0);
		jp1.add(jll);
		jp1.add(jl02);

		jp1.add(jl1);jp1.add(jtf);jp1.add(jb4);//在面板中添加9个组件
		jp1.add(jl2);jp1.add(jpf);jp1.add(jl3);
		jp1.add(jc1);jp1.add(jc2);jp1.add(jl4);


		jtp=new JTabbedPane();//创建选项卡面板
		jtp.add(jp1,"YY号码");//在选项卡中添加3个JPanel面板
		jp2=new JPanel();
		jp3=new JPanel();
		jtp.add(jp2,"手机号码");
		jtp.add(jp3,"电子邮箱");
		this.add(jtp,"Center");//选项卡面板添加到窗体中部

		jb1=new JButton(new ImageIcon("com.yychat.view/image/login.gif"));//图片按钮
	    jb1.addActionListener(this);

		jb2=new JButton(new ImageIcon("com.yychat.view/image/register.gif"));
		jb2.addActionListener(this);
		jb3=new JButton(new ImageIcon("com.yychat.view/image/cancel.jpg"));

		jp=new JPanel();//创建面板对象
		jp.add(jb1);//在JPanel面板中添加按钮
		jp.add(jb2);//默认FlowLayout 流式布局
		jp.add(jb3);

		this.add(jp,"South");//JPanel组件添加到窗体南部


		Image im=new ImageIcon("com.yychat.view/image/duck2.gif").getImage();//创建Image对象
		this.setIconImage(im);//设置窗体左上角图标

		//this.setBounds(800, 600, 350, 250);//设置窗体边界（位置和大小）
		this.setLocationRelativeTo(null);//设置相对位置为null
		this.setSize(550,420);//窗体大小
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);//点击窗体的关闭按钮，退出程序
		this.setTitle("YY聊天");//设置窗体标题
		this.setVisible(true);//窗体可视
	}

	public static void main(String[] args) {

		ClientLogin cl=new ClientLogin();//创建窗体对象

	}


	public void actionPerformed(ActionEvent arg0){


		String name=jtf.getText();
		String password=new String(jpf.getPassword());
		User user= new User();
		user.setUserName(name);
		user.setPassword(password);

		if(arg0.getSource()==jb1)
		{
			user.setUserType(UserType.USER_LOGIN_VALIDATE);

			Message mess=new YychatClientConnection().loginValidate1(user);
			if(mess.getMessageType().equals(MessageType.LOGIN_VALIDATE_SUCCESS)) {
				String allFriend = mess.getContent();
				FriendList fl = new FriendList(name, allFriend);
                hmFriendList.put(name, fl);
				this.dispose();
            }else {
                JOptionPane.showMessageDialog(this,"密码错误,请重新登陆!");
            }
		}
		if(arg0.getSource()==jb2) {
           user.setUserType(UserType.USER_REGISTER);
           if (new YychatClientConnection().registerUser(user)) {
               JOptionPane.showMessageDialog(this, name + "注册成功!");
           } else {
               JOptionPane.showMessageDialog(this, name + "已存在,请重新注册!");
           }
		}
	}

	private void sendMessage(Socket s, Message mess) {
		ObjectOutputStream oos;
		try{
			oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(mess);
		} catch (IOException e) {
            e.printStackTrace();
        }
    }
}