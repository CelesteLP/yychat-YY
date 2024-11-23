package com.yychat.view;

import com.yychat.control.YychatClientConnection;
import com.yychat.model.Message;
import com.yychat.model.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FriendList extends JFrame implements
ActionListener,MouseListener, WindowListener{
	public static HashMap<String,FriendChat>hmFriendChat=new HashMap<String,FriendChat>();
	//定义卡片1(好友面板)中的组件
	JPanel friendPanel;//卡片1中组件的容器
	JButton myFriendButton1;
	JButton myStrangerButton1;
	JButton blackListButton1;
	JScrollPane friendListScrollPane;//好友列表的滚动条面板
	JPanel friendListPanel;//好友列表面板
	final int MYFRIENDCOUNT=50;//50个好友
	JLabel friendLabel[]=new JLabel[MYFRIENDCOUNT];//定义好友图标数组
	//定义卡片2（陌生人面板）中的组件
	JPanel strangerPanel;//卡片2中组件的容器
	JButton myFriendButton2;//卡片2中组件的容器
	JButton myStrangerButton2;
	JButton blackListButton2;
	JScrollPane strangerListScrollPane;//陌生人的滚动条面板
	JPanel strangerListPanel;//陌生人列表
	final int STRANGERCOUNT=20;//20个陌生人
	JLabel strangerLabel[]=new JLabel [STRANGERCOUNT];
	CardLayout cl;//cl声明为成员变量

	String name;

    JPanel addFriendPanel;
    JButton addFriendButton;
	public FriendList(String name,String allFriend){

		this.name=name;
		//1、创建卡片1（好友面板）中的组件
		friendPanel=new JPanel(new BorderLayout());//卡片1设置边界布局模式
		addFriendPanel=new JPanel(new GridLayout(2,1));
        addFriendButton=new JButton("添加好友");
		addFriendButton.addActionListener(this);
		myFriendButton1=new JButton("我的好友");
		addFriendPanel.add(addFriendButton);
        addFriendPanel.add(myFriendButton1);
        friendPanel.add(addFriendPanel,"North");

		friendListPanel=new JPanel();
        showAllFriend(allFriend);


		friendListScrollPane=new JScrollPane(friendListPanel);//创建好友滚动条面板
		friendPanel.add(friendListScrollPane,"Center");//好友滚动条添加到卡片1的中部
		myStrangerButton1=new JButton("陌生人");
		myStrangerButton1.addActionListener(this);
		blackListButton1=new JButton("黑名单");
		//为了容纳myStrangerButton1和blackListButton1定义一个新JPanel面版
		JPanel stranger_BlackPanel=new JPanel(new GridLayout(2,1));//2行1列的网格布局
		stranger_BlackPanel.add(myStrangerButton1);
		stranger_BlackPanel.add(blackListButton1);
		friendPanel.add(stranger_BlackPanel,"South");//添加到卡片1的南部
		//2、创建卡片2（陌生人面板）中的组件
		strangerPanel=new JPanel(new BorderLayout());
		myFriendButton2=new JButton("我的好友");
		myFriendButton2.addActionListener(this);
		myStrangerButton2=new JButton("陌生人");
		//为了容纳myFriendButton2和myStrangerButton2定义一个新JPanel面板
		JPanel friend_strangerPanel=new JPanel(new GridLayout(2,1));//2行1列的网格布局
		friend_strangerPanel.add(myFriendButton2);
		friend_strangerPanel.add(myStrangerButton2);
		strangerPanel.add(friend_strangerPanel,"North");//添加到卡片2的北部
		//创建中间的陌生人列表滚动条面板
		strangerListPanel=new JPanel(new GridLayout(STRANGERCOUNT,1));
		for(int i=0;i<strangerLabel.length;i++){
			strangerLabel[i]=new JLabel(i+"号陌生人",new ImageIcon("com.yychat.view/image/tortoise.gif"),JLabel.LEFT);
			strangerListPanel.add(strangerLabel[i]);
		}
		strangerListScrollPane=new JScrollPane(strangerListPanel);
		strangerPanel.add(strangerListScrollPane,"Center");//陌生人滚动条添加到卡片2的中部
		blackListButton2=new JButton("黑名单");
		strangerPanel.add(blackListButton2,"South");//添加到卡片2的南部
		//3、创建卡片布局
		cl=new CardLayout();
		this.setLayout(cl);//窗体设置为卡片布局
		this.add(friendPanel,"card1");//窗体中添加卡片
		this.add(strangerPanel,"card2");
		this.setIconImage(new ImageIcon("com.yychat.view/image/duck2.gif").getImage());
		this.setTitle(name+"的好友列表");
		//this.setTitle("好友列表);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setLocationRelativeTo(null);
		this.addWindowListener(this);
		this.setBounds(1800,150,600,1100);
		this.setVisible(true);
	}

	public void showAllFriend(String allFriend) {
		String [] myFriend=allFriend.split(" ");
		friendListPanel.removeAll();
		friendListPanel.setLayout(new GridLayout(myFriend.length-1,1));
		for(int i=1;i<myFriend.length;i++){
			String imageStr="com.yychat.view/image/"+i%6+".jpg";//随机生成图片路径
			ImageIcon imageIcon=new ImageIcon(imageStr);
			friendLabel[i]=new JLabel(myFriend[i]+"",imageIcon,JLabel.LEFT);
			//if(i!=Integer.valueOf(name))
			friendLabel[i].addMouseListener(this);
			friendListPanel.add(friendLabel[i]);//好友图标添加到好友列表面板中
		}
		friendListPanel.revalidate();
	}

	public static void main(String[] args) {
		//FriendList fl=new FriendList();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == myFriendButton2) {
			cl.show(this.getContentPane(), "card1");
			if (arg0.getSource() == myStrangerButton1)
				cl.show(this.getContentPane(), "card2");
		}
		if (arg0.getSource() == myStrangerButton1) {
			cl.show(this.getContentPane(), "card2");
		}
		if (arg0.getSource() == addFriendButton) {
			String newFriend=JOptionPane.showInputDialog(this, "请输入要添加的好友名字");
			if (newFriend != null) {
				Message mess=new Message();
				mess.setSender(name);
				mess.setReceiver(newFriend);
				mess.setContent(newFriend);
				mess.setMessageType(MessageType.ADD_NEW_FRIEND);

				try {
					ObjectOutputStream oos=new ObjectOutputStream(YychatClientConnection.s.getOutputStream());
					oos.writeObject(mess);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void mouseClicked(MouseEvent arg0){
		if(arg0.getClickCount()==2){//双击鼠标左键时
			JLabel jl=(JLabel)arg0.getSource();//获得被双击好友的标签组件
			String toName=jl.getText();//拿到好友的名字
			//new FriendChat(name,toName);
			FriendChat fc=new FriendChat(name,toName);
			hmFriendChat.put(name+"to"+toName, fc);
		}
	}
	public void mouseEntered(MouseEvent arg0){//鼠标进入好友标签组件时
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.red);//好友名字为红色
	}
	public void mouseExited(MouseEvent arg0){//鼠标离开好友标签组件时
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.blue);//好友名字为蓝色
	}
	public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println(name+" 准备关闭客户端...");
		Message mess=new Message();
		mess.setSender(name);
		mess.setReceiver("Server");
		mess.setMessageType(MessageType.USER_EXIT_SERVER_THREAD_CLOSE);
		ObjectOutputStream oos;
		try {
			oos=new ObjectOutputStream(YychatClientConnection.s.getOutputStream());
			oos.writeObject(mess);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}

	public void activeOnlineFriendIcon(Message mess){
		String onlineFriend=mess.getContent();
		String[] onlineFriendName=onlineFriend.split(" ");

		for (int i = 1; i < onlineFriendName.length; i++) {
			//this.friendLabel[Integer.valueOf(onlineFriendName[i])].setEnabled(true);
		}
	}


	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	public void activeNewOnlineFriendIcon(String newOnlineFriend) {
		//this.friendLabel[Integer.valueOf(newOnlineFriend)].setEnabled(true);
	}
}