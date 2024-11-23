package com.yychat.view;

import com.yychat.control.YychatClientConnection;
import com.yychat.model.Message;
import com.yychat.model.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
public class FriendChat extends JFrame implements ActionListener,WindowListener {
	JTextArea jta;
	JScrollPane jsp;
	JTextField jtf;
	JButton jb;
	String sender;
	String receiver;
	JTextArea inputArea;
	JTextArea chatArea;
	JButton sendButton;
	public FriendChat(String sender,String receiver){
		this.sender=sender;
		this.receiver=receiver;

		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setForeground(Color.red);

		Font largeFont=new Font("Serif",Font.PLAIN,30);
		chatArea.setFont(largeFont);

		JScrollPane scrollPane = new JScrollPane(chatArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);

		//chatArea.append("dadasd");
		// 初始化发送按钮
		inputArea = new JTextArea(5, 20);
		inputArea.setLineWrap(true);
		inputArea.setWrapStyleWord(true);
		inputArea.setFont(largeFont);
		JScrollPane inputScrollPane = new JScrollPane(inputArea);
		inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// 初始化发送按钮
		sendButton = new JButton("发送");
		sendButton.addActionListener(this);
		sendButton.setForeground(Color.blue);

		// 将输入文本区域和发送按钮放在同一个JPanel中
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout()); // 使用BorderLayout布局管理器
		southPanel.add(inputScrollPane, BorderLayout.CENTER);
		southPanel.add(sendButton, BorderLayout.EAST); // 或者使用BorderLayout.SOUTH

		this.add(southPanel, BorderLayout.SOUTH);

		// 设置窗口大小和位置
		this.setSize(1000, 1000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle(sender + " to " + receiver + " 的聊天界面");
		this.setIconImage(new ImageIcon("ClientLogin/image/duck2.gif").getImage());

		this.addWindowListener(this);
		this.setVisible(true);
	}

	public static void main(String[] args){
		//Friendchat fc=new FriendChat();
	}
	public void append(Message mess){
		System.out.println(mess.getSender()+" "+mess.getReceiver());
		chatArea.append(mess.getSendTime().toString()+"\r\n"+mess.getSender()+" 对你说："+mess.getContent()+"\r\n");
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==sendButton){//点击发送按钮
			String messageText = inputArea.getText(); // 注意这里使用了inputArea
			if(!messageText.trim().isEmpty()){

				chatArea.append(messageText + "\r\n");

				Message mess=new Message();
				mess.setSender(sender);
				mess.setReceiver(receiver);
				mess.setContent(inputArea.getText());
				mess.setMessageType(MessageType.COMMON_CHAT_MESSAGE);//设置聊天信息的类型

				inputArea.setText("");
				try {
					OutputStream os=YychatClientConnection.s.getOutputStream();
					ObjectOutputStream oos=new ObjectOutputStream(os);
					oos.writeObject(mess);//通过Socket对象发送聊天信息到服务器端
				} catch (IOException el) {
					el.printStackTrace();
				}
			}
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println(sender+" 准备关闭客户端...");
		Message mess=new Message();
		mess.setSender(sender);
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
}

