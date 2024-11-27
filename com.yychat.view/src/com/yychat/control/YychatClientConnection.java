package com.yychat.control;

import com.yychat.model.Message;
import com.yychat.model.MessageType;
import com.yychat.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
public class YychatClientConnection {
	public static Socket s;
	public YychatClientConnection() {
		try {
			s=new Socket("127.0.0.1",3456);//创建Socket对象，和服务器建立连接
            //s=new Socket("192.168.1.103",3456);
			System.out.println("客户端连接成功: "+s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean registerUser(User user) {
        boolean registerSuccess=false;
        try{
            OutputStream os=s.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(user);

            ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
            Message mess=(Message) ois.readObject();

            if(mess.getMessageType().equals(MessageType.USER_REGISTER_SUCCESS)){
                registerSuccess=true;
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return registerSuccess;
    }

	public Message loginValidate1(User user){
        Message mess=null;
        try {
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(user);
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            mess = (Message) ois.readObject();
            if (mess.getMessageType().equals(MessageType.LOGIN_VALIDATE_SUCCESS)) {
                new ClientReceiverThread(s).start();
            } else {
                s.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return mess;
    }
}