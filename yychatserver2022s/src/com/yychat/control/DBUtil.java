package com.yychat.control;

import com.yychat.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBUtil {
    public static String db_url="jdbc:mysql://localhost:3306/yychat2022s?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";

    public static String db_user="root";
    public static String db_password="123456";

    public static Connection conn=getConnection();

    private static Connection getConnection() {
        Connection conn=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(db_url,db_user,db_password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return conn;
    }
    public static void saveMessage(Message mess) {
        String message_insert_str = "insert into message(sender,receiver,content,sendtime) values(?,?,?,?)";
        PreparedStatement psmt;
        try {
            psmt = conn.prepareStatement(message_insert_str);
            psmt.setString(1, mess.getSender());
            psmt.setString(2, mess.getReceiver());
            psmt.setString(3, mess.getContent());
            Date sendTime = mess.getSendTime();
            //ToDo
            //psmt.setString(4, new Timestamp(mess.getSendTime().getTime()).toString());
            psmt.setTimestamp(4,new Timestamp(sendTime.getTime()));
            psmt.execute();
        } catch (SQLException e){
        e.printStackTrace();
        }
    }



    public static boolean loginValidate(String username,String password){
        boolean loginSuccess=false;
        String user_query_str="select * from user where username=? and password=?";
        PreparedStatement psmt;
        try{
            psmt=conn.prepareStatement(user_query_str);
            psmt.setString(1,username);
            psmt.setString(2,password);
            ResultSet rs=psmt.executeQuery();
            loginSuccess=rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return loginSuccess;
    }
    public static boolean seekUser(String username){
        boolean seekSuccess=false;
        String user_query_str="select * from user where username=?";
        PreparedStatement psmt;
        try{
            psmt=conn.prepareStatement(user_query_str);
            psmt.setString(1,username);
            ResultSet rs=psmt.executeQuery();
            seekSuccess=rs.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return seekSuccess;
    }
    public static int insertIntoUser(String username,String password){
        int count=0;
        String user_inset_into_str="insert into user(username,password) values(?,?)";
        PreparedStatement psmt;
        try{
            psmt=conn.prepareStatement(user_inset_into_str);
            psmt.setString(1,username);
            psmt.setString(2,password);
            //ddd
            count=psmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static String seekAllFriend(String userName, int FriendType) {
        String allFriend="";
        String userrelation_query_str="select slaveuser from userrelation where masteruser=? and relation=?";

        PreparedStatement psmt;
        try{
            psmt=conn.prepareStatement(userrelation_query_str);
            psmt.setString(1,userName);
            psmt.setInt(2,FriendType);
            ResultSet rs=psmt.executeQuery();
            while (rs.next())
                allFriend=allFriend+" "+rs.getString(1);

            System.out.println(userName+" 全部好友: "+allFriend);

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return allFriend;
    }
    public static boolean seekFriend(String sender,String newFriend,int friendType){
        boolean seekSuccess=false;
        String userrelation_query_str="select * from userrelation where masteruser=? and slaveuser=? and relation=?";
        PreparedStatement psmt;
        try{
            psmt=conn.prepareStatement(userrelation_query_str);
            psmt.setString(1,sender);
            psmt.setString(2,newFriend);
            psmt.setInt(3,friendType);
            ResultSet rs=psmt.executeQuery();
            seekSuccess=rs.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return seekSuccess;
    }
    public static int insertIntoFriend(String sender,String newFriend,int friendType){
        int count=0;
        String userrelation_insert_into_str="insert into userrelation(masteruser,slaveuser,relation) values(?,?,?)";

        try{
            PreparedStatement psmt=conn.prepareStatement(userrelation_insert_into_str);
            psmt.setString(1,sender);
            psmt.setString(2,newFriend);
            psmt.setInt(3,friendType);
            count=psmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<String> ViewHistory(String sender,String receiver) {
        List<String> history=new ArrayList<String>();
        //查询我给他发的信息
        String history_query_str =
                "SELECT * " +
                        "FROM (" +
                        "(SELECT * FROM message WHERE sender=? AND receiver=?) " +
                        "UNION " +
                        "(SELECT * FROM message WHERE sender=? AND receiver=?) " +
                        ") AS combined " +
                        "ORDER BY sendtime ASC";
        PreparedStatement psmt;

        try{
            //查询我给他发的信息
            psmt=conn.prepareStatement(history_query_str);
            //填入占位符
            psmt.setString(1,sender);
            psmt.setString(2,receiver);
            psmt.setString(3,receiver);
            psmt.setString(4,sender);
            //获得输入流
            ResultSet rs=psmt.executeQuery();

            //将我发的信息插入history中
            while (rs.next()) {
                history.add(rs.getString(5) + "~" + rs.getString(2) + "~" + rs.getString(3) + "~" + rs.getString(4));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }
    public static boolean FriendStatus(String sender,String receiver){
        boolean friendStatus=true;
        String userrelation_query_str="select * from userrelation where masteruser=? and slaveuser=?";
        PreparedStatement psmt;
        try{
            psmt=conn.prepareStatement(userrelation_query_str);
            psmt.setString(1,receiver);
            psmt.setString(2,sender);
            boolean rs=psmt.executeQuery().next();
            if(!rs){
                friendStatus=false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return friendStatus;
    }

    public static void RemoveFriend(String sender,String receiver){
        String userrelation_delete_str="delete from userrelation where masteruser=? and slaveuser=?";
        PreparedStatement psmt;
        try{
            psmt=conn.prepareStatement(userrelation_delete_str);
            psmt.setString(1,sender);
            psmt.setString(2,receiver);
            psmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}