package com.yychat.control;
import java.io.*;

import com.yychat.model.*;

import java.net.*;
import java.sql.*;
import java.util.HashMap;

public class YychatServer {
    public static HashMap hmSocket = new HashMap<String, Socket>();
    ServerSocket ss;
    Socket s;

    public YychatServer() {
        try {
            ss = new ServerSocket(3456);
            System.out.println("服务器启动成功，正在监听3456端口...");
            while (true) {
                s = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                User user = (User) ois.readObject();
                String userName = user.getUserName();
                String password = user.getPassword();
                System.out.println(userName + " 连接成功: " + s);
                System.out.println("服务端接收到的客户端登陆信息userName:" + userName + " password:" + password);

                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                Message mess = new Message();

                // 注册
                if (user.getUserType().equals(UserType.USER_REGISTER)) {
                    if (DBUtil.seekUser(userName)) {
                        mess.setMessageType(MessageType.USER_REGISTER_FAILURE);
                    } else {
                        DBUtil.insertIntoUser(userName, password);
                        mess.setMessageType(MessageType.USER_REGISTER_SUCCESS);
                    }
                    oos.writeObject(mess);
                    s.close();
                }

                // 登陆
                if (user.getUserType().equals(UserType.USER_LOGIN_VALIDATE)) {
                    boolean loginSuccess = DBUtil.loginValidate(userName, password);
                    if (loginSuccess) {
                        System.out.println("密码验证通过!");
                        String allFriend = DBUtil.seekAllFriend(userName, 1);
                        mess.setContent(allFriend);
                        mess.setMessageType(MessageType.LOGIN_VALIDATE_SUCCESS);

                        oos.writeObject(mess);
                        hmSocket.put(userName, s);
                        new ServerReceiverThread(s).start();
                        System.out.println("启动线程成功!");
                    } else {
                        System.out.println("密码验证失败!");
                        mess.setMessageType(MessageType.LOGIN_VALIDATE_FAILURE);
                        oos.writeObject(mess);
                        s.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
