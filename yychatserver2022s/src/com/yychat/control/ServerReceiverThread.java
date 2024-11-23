package com.yychat.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.yychat.model.*;

import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Set;

public class ServerReceiverThread extends Thread {
    Socket s;

    public ServerReceiverThread(Socket s) {
        this.s = s;
    }

    public void run() {//定义线程方法
        while (true) {//在线程中不断接收客户端发送来的信息
            try {
                //创建对象输出流对象
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message mess = (Message) ois.readObject();//接收Message对象

                if (mess.getMessageType().equals(MessageType.ADD_NEW_FRIEND)) {
                    String sender = mess.getSender();
                    String newFriend = mess.getContent();
                    //System.out.println("--------");
                    if (DBUtil.seekUser(newFriend)) {
                        if (DBUtil.seekFriend(sender, newFriend, 1)) {
                            mess.setMessageType(MessageType.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND);
                        } else {
                            DBUtil.insertIntoFriend(sender, newFriend, 1);
                            String allFriend = DBUtil.seekAllFriend(sender, 1);
                            mess.setContent(allFriend);
                            mess.setMessageType(MessageType.ADD_NEW_FRIEND_SUCCESS);
                        }
                    } else {
                        mess.setMessageType(MessageType.ADD_NEW_FRIEND_FAILURE_NO_USER);
                    }
                    Socket ss = (Socket) YychatServer.hmSocket.get(sender);
                    sendMessage(ss, mess);
                }

                if(mess.getMessageType().equals(MessageType.COMMON_CHAT_MESSAGE)){
                    System.out.println(mess.getSender() + " 对 " + mess.getReceiver() + "说:" + mess.getContent());
                    mess.setSendTime(new java.util.Date());
                    DBUtil.saveMessage(mess);
                    String receiver = mess.getReceiver();
                    Socket rs = (Socket) YychatServer.hmSocket.get(receiver);
                    System.out.println("接收方" + receiver + "的Socket对象：" + rs);
                    if (rs != null) {
                        ObjectOutputStream oos = new ObjectOutputStream(rs.getOutputStream());
                        oos.writeObject(mess);
                    }else{
                        System.out.println(receiver + "不在线");
                    }
                }


                if (mess.getMessageType().equals(MessageType.USER_EXIT_SERVER_THREAD_CLOSE)) {
                    String sender = mess.getSender();
                    mess.setMessageType(MessageType.USER_EXIT_CLIENT_THREAD_CLOSE);
                    sendMessage(s, mess);
                    System.out.println(sender + "用户退出了!正在关闭其服务线程");
                    s.close();
                    break;
                }


                if (mess.getMessageType().equals(MessageType.REQUEST_ONLINE_FRIEND)) {
                    Set onlineFriendSet = YychatServer.hmSocket.keySet();
                    Iterator it = onlineFriendSet.iterator();
                    String onlineFriend = "";
                    while (it.hasNext()) {
                        onlineFriend += " " + it.next();
                    }
                    mess.setReceiver(mess.getSender());
                    mess.setSender("Server");
                    mess.setMessageType(MessageType.RESPONSE_ONLINE_FRIEND);
                    mess.setContent(onlineFriend);
                    sendMessage(s, mess);
                }
                if (mess.getMessageType().equals(MessageType.NEW_ONLINE_TO_ALL_FRIEND)) {
                    mess.setMessageType(MessageType.NEW_ONLINE_FRIEND);
                    Set onlineFriends = YychatServer.hmSocket.keySet();
                    Iterator it = onlineFriends.iterator();
                    while (it.hasNext()) {
                        String receiver = (String) it.next();
                        mess.setReceiver(receiver);
                        Socket rs = (Socket) YychatServer.hmSocket.get(receiver);
                        sendMessage(rs, mess);
                    }
                }

            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Socket s, Message mess) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

