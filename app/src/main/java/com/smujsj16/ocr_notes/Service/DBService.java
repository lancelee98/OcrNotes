package com.smujsj16.ocr_notes.Service;


import android.provider.ContactsContract;
import android.util.Log;


import com.smujsj16.ocr_notes.Entity.Info;
import com.smujsj16.ocr_notes.Entity.User;
import com.smujsj16.ocr_notes.utils.CheckUtils;
import com.smujsj16.ocr_notes.utils.MysqlUtils;
import com.smujsj16.ocr_notes.utils.SFTPUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

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

    public int getUserId(User user)
    {
        String sql="SELECT * FROM user WHERE phone_num=? ";
        int result=-1;
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
                            result=1;
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
    public int createNewNotes(Info info){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatStr =formatter.format(new java.util.Date());
        int result=-1;
        Long userid=Long.valueOf(info.getUser_id());
        String title=info.getTitle();
        String ocrcontent=info.getOcr_content();
        info.setCreate_time(new java.util.Date());
        Timestamp create_time = new Timestamp(info.getCreate_time().getTime());
        String localPath=info.getImage_link();
        String image_name=info.getImage_name();
        String remote_name=info.getUser_id()+formatStr+image_name.substring(image_name.length()-5);
        info.setImage_link(remote_name);

        if(true){//!CheckUtils.isMobile(phone_num)&&CheckUtils.isPassword(password)  以后添加条件
            //获取链接数据库对象
            conn= MysqlUtils.getConn();
            //MySQL 语句
            String sql="INSERT INTO info (user_id,title,image_link,create_time,ocr_content) VALUES (?,?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if(conn!=null&&(!conn.isClosed())){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setLong(1,userid);//第一个参数
                    ps.setString(2,title);//第二个参数
                    ps.setString(3,remote_name);//第一个参数
                    ps.setTimestamp(4,create_time);//第二个参数
                    ps.setString(5,ocrcontent);//第一个参数
                    result=ps.executeUpdate();//返回1 执行成功
                    SFTPUtils sftp = new SFTPUtils();
                    Log.d(TAG,"上传文件");
                    sftp.connect();
                    Log.d(TAG,"连接成功");
                    sftp.uploadFile(remote_name, localPath, image_name);
                    Log.d(TAG,"上传成功");
                    sftp.disconnect();
                    Log.d(TAG,"断开连接");

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MysqlUtils.closeAll(conn,ps);//关闭相关操作
        return result;
    }



    public List<Info> selectNotes(String condition,String user_id){
        //结果存放集合
        List<Info> list=new ArrayList<Info>();
        String sql="select * from info where user_id=?";
        //获取链接数据库对象
        conn= MysqlUtils.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                //ps.setString(1,condition);
                ps.setString(1,user_id);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            Info info=new Info();
                            info.setUser_id(String.valueOf(rs.getLong(1)));
                            info.setInfo_id(rs.getLong(2));
                            info.setTitle(rs.getString(3));
                            info.setType(rs.getInt(4));
                            info.setParent_id(rs.getInt(5));
                            info.setImage_link("http://118.89.37.35:58123/"+rs.getString(6));
                            info.setCreate_time(rs.getTimestamp(7));
                            info.setOcr_content(rs.getString(8));
                            list.add(info);
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