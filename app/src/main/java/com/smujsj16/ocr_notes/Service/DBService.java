package com.smujsj16.ocr_notes.Service;


import com.smujsj16.ocr_notes.Entity.User;
import com.smujsj16.ocr_notes.utils.MysqlUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBService {

    private Connection conn=null; //打开数据库对象
    private PreparedStatement ps=null;//操作整合sql语句的对象
    private ResultSet rs=null;//查询结果的集合

    //DBService 对象
    public static DBService dbService=null;

    /**
     * 构造方法 私有化
     * */

    private DBService(){

    }

    /**
     * 获取MySQL数据库单例类对象
     * */

    public static DBService getDbService(){
        if(dbService==null){
            dbService=new DBService();
        }
        return dbService;
    }


    public int checkPassword(String phone_num, String password){
        int result=-1;
        //获取链接数据库对象
        conn= MysqlUtils.getConn();
        //MySQL 语句
        String sql="select * from user where phone_num=?";
        try {
            boolean closed=conn.isClosed();
            if(conn!=null&&(!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1, phone_num);//第一个参数state 一定要和上面SQL语句字段顺序一致
                if (ps != null) {
                    rs = ps.executeQuery();
                    while(rs.next()){
                        if(rs.getString(1).equals(password))
                        result=1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtils.closeAll(conn,ps,rs);//关闭相关操作
        return result;
    }

    /**
     * 检验密码
     * */

    public String getUserId(User user)
    {
        String sql="SELECT * FROM user WHERE phone_num=? ";
        String result="-1";
        String phone_num=user.getPhone_number();
        conn= MysqlUtils.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                ps.setString(1,phone_num);//第一个参数
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            user.setUser_id(rs.getString(3));
                            result="1";
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtils.closeAll(conn,ps,rs);//关闭相关操作
        return result;
    }

    public int createNewUser(User user){
        int result=-1;
        String phone_num=user.getPhone_number();
        String password=user.getPassword();
        if(true){//!CheckUtils.isMobile(phone_num)&&CheckUtils.isPassword(password)  以后添加条件
            //获取链接数据库对象
            conn= MysqlUtils.getConn();
            //MySQL 语句
            String sql="INSERT INTO user (phone_num,password) VALUES (?,?)";
            try {
                boolean closed=conn.isClosed();
                if(conn!=null&&(!conn.isClosed())){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1,phone_num);//第一个参数
                    ps.setString(2,password);//第二个参数
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MysqlUtils.closeAll(conn,ps);//关闭相关操作
        return result;
    }



        /*public List<User> selectNotes(String condition){
        //结果存放集合
        List<User> list=new ArrayList<User>();
        //MySQL 语句
        //String sql="select "/condition" from user";
        //获取链接数据库对象
        conn= MysqlUtils.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            User u=new User();
                            u.setId(rs.getString("id"));
                            u.setName(rs.getString("name"));
                            u.setPhone(rs.getString("phone"));
                            u.setContent(rs.getString("content"));
                            u.setState(rs.getString("state"));
                            list.add(u);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtils.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }*/


    /**
     * 获取要发送短信的患者信息    查
     * */

    /*public List<User> getUserData(){
        //结果存放集合
        List<User> list=new ArrayList<User>();
        //MySQL 语句
        String sql="select * from user";
        //获取链接数据库对象
        conn= MysqlUtils.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            User u=new User();
                            u.setId(rs.getString("id"));
                            u.setName(rs.getString("name"));
                            u.setPhone(rs.getString("phone"));
                            u.setContent(rs.getString("content"));
                            u.setState(rs.getString("state"));
                            list.add(u);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlUtils.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }

    /**
     * 修改数据库中某个对象的状态   改
     * */

    /*public int updateUserData(String phone,String password){
        int result=-1;
        if(!StringUtils.isEmpty(phone)){
            //获取链接数据库对象
            conn= MysqlUtils.getConn();
            //MySQL 语句
            String sql="update user set state=? where phone=?";
            try {
                boolean closed=conn.isClosed();
                if(conn!=null&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1,"1");//第一个参数state 一定要和上面SQL语句字段顺序一致
                    ps.setString(2,phone);//第二个参数 phone 一定要和上面SQL语句字段顺序一致
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MysqlUtils.closeAll(conn,ps);//关闭相关操作
        return result;
    }

    /**
     * 批量向数据库插入数据   增
     * */

   /* public int insertUserData(List<User> list){
        int result=-1;
        if((list!=null)&&(list.size()>0)){
            //获取链接数据库对象
            conn= MysqlUtils.getConn();
            //MySQL 语句
            String sql="INSERT INTO user (name,phone,content,state) VALUES (?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    for(User user:list){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
                        String name=user.getName();
                        String phone=user.getPhone();
                        String content=user.getContent();
                        String state=user.getState();
                        ps.setString(1,name);//第一个参数 name 规则同上
                        ps.setString(2,phone);//第二个参数 phone 规则同上
                        ps.setString(3,content);//第三个参数 content 规则同上
                        ps.setString(4,state);//第四个参数 state 规则同上
                        result=ps.executeUpdate();//返回1 执行成功
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MysqlUtils.closeAll(conn,ps);//关闭相关操作
        return result;
    }


    /**
     * 删除数据  删
     * */

    /*public int delUserData(String phone){
        int result=-1;
        if((!StringUtils.isEmpty(phone))&&(PhoneNumberUtils.isMobileNumber(phone))){
            //获取链接数据库对象
            conn= MysqlUtils.getConn();
            //MySQL 语句
            String sql="delete from user where phone=?";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1, phone);
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MysqlUtils.closeAll(conn,ps);//关闭相关操作
        return result;
    }*/

}
