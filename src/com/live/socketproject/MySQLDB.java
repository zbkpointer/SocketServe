package com.live.socketproject;

/**
 * Created by Administrator on 2018/6/13.
 */
import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;

public class MySQLDB {

    // JDBC 驱动名及数据库 URL
    private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://localhost:3306/alertinformation?useSSL=false";
    // 数据库的用户名与密码，需要根据自己的设置
    private static String USER = "root";
    private static String PASS = "199387";

    //开启数据库连接
    public static  Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("数据库连接成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static void insert(AlertInformation alert) {
        Connection conn = getConn();
        int count =0;
        String querySql="select * from alertinformation";
        String sql = "insert into alertinformation (id,idofbuilding,cellofbuilding,idofroom,alertcategory,alertlevel,alerttime,alertstatus,alertstamp) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt =(PreparedStatement)conn.prepareStatement(querySql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                count++;
            }
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1,Integer.toString(count+1));
            pstmt.setString(2, alert.getIdOfBuilding());
            pstmt.setString(3,alert.getCellOfBuilding());
            pstmt.setString(4, alert.getIdOfRoom());
            pstmt.setString(5, alert.getAlertCategory());
            pstmt.setString(6, alert.getAlertLevel());
            pstmt.setString(7, Time.getTime());
            pstmt.setString(8,"未处理");
            pstmt.setString(9, String.valueOf(System.currentTimeMillis()));
            pstmt.executeUpdate();

            System.out.println("报告成功");
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void update(HashSet<String> hashSet){
        Connection conn = getConn();
        Iterator iterator = hashSet.iterator();//获取迭代器
        String querySql="select * from MacAddress";
        PreparedStatement pstmt;

        int count =0;

        try {

        pstmt =(PreparedStatement)conn.prepareStatement(querySql);
        ResultSet rs = pstmt.executeQuery();
        /**
         * 扫描整张表获取记录数
         */
        while(rs.next()){
            count++;
        }
        /**更新整张表状态为下线
         *
         */
        while(count != 0){
            String sql = "update MacAddress set State = '0' where ID = "+ String.valueOf(count);
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

            count--;
        }



        while(iterator.hasNext()) {

            String sql = "update MacAddress set State = '1' where MacAddress = "+ iterator.next();
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            System.out.println("更新成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



    public static void main(String[] args){
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("444553540001");
        hashSet.add("444553540002");
        hashSet.add("444553540003");


        MySQLDB.update(hashSet);



    }








}
