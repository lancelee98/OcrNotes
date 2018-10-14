package com.smujsj16.ocr_notes.utils;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlUtils {

    private static String driver = "com.mysql.jdbc.Driver";//MySQL 驱动
    private static String url ="jdbc:mysql://这里填服务器ip:3306/ocr_app?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static String user = "root";//用户名
    private static String password = "root";//密码

    public static Connection getConn(){
        Connection conn = null;
        try {
            Log.d("lance","正在获取MYSQL驱动");
            Class.forName(driver).newInstance();//获取MYSQL驱动
            Log.d("lance","获取MYSQL驱动成功");
            conn = (Connection) DriverManager.getConnection(url, user, password);//获取连接
            Log.d("lance","连接数据库成功");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库
     * */

    public static void closeAll(Connection conn, PreparedStatement ps){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 关闭数据库
     * */

    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
