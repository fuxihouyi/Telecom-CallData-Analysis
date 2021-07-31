package com.atguigu.ct.cache;

import com.atguigu.ct.common.util.JDBCUtil;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 启动缓存客户端，想redis中增加缓存数据
 */
public class Bootstrap {
    public static void main(String[] args) {

        //1. 读取Mysql中的数据
        Map<String, Integer> userMap = new HashMap<String, Integer>();
        Map<String, Integer> dateMap = new HashMap<String, Integer>();

        //定义一个sql操作类
        PreparedStatement pstat = null;

        //定义一个sql结果存储类
        ResultSet rs = null;

        //定义一个链接类
        Connection connection = null;

        try{
            //获取配置类
            connection = JDBCUtil.getConnection();

            //读取数据，时间数据
            String queryUserSql = "select id, tel from ct_user";

            //实例化sql操作类并注册要执行的sql语句
            pstat = connection.prepareStatement(queryUserSql);

            //通过sql操作对象提交sql语句，并用前面定义的sql结果存储类存储sql语句返回的结果
            rs = pstat.executeQuery();

            //遍历sql的结果对象
            while(rs.next()){
                Integer id = rs.getInt(1);
                String tel = rs.getString(2);
                userMap.put(tel, id);
            }

            //关闭sql的输出源
            rs.close();

            //设计sql语句
            String queryDateSql = "select id, year, month, day from ct_date";

            //关闭上一个sql输出源后，即可开始新的sql，即注册一个新的sql语句（操作）
            pstat = connection.prepareStatement(queryDateSql);

            //执行新的sql语句
            rs = pstat.executeQuery();

            //遍历sql的结果对象
            while(rs.next()){
                Integer id = rs.getInt(1);
                String year = rs.getString(2);
                String month = rs.getString(3);
                //月数格式规范
                if (month.length() == 1){
                    month = "0" + month;
                }
                String day = rs.getString(4);
                //天数格式规范
                if (day.length() == 1){
                    day = "0" + day;
                }
                dateMap.put(year+month+day, id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if( pstat != null){
                try {
                    pstat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(userMap.size());
        System.out.println(dateMap.size());

        //2. 向redis中存储数据
        //构建redis的api，注册redis的接口
        Jedis jedis = new Jedis("master", 6379);

        //构建一个迭代器接收Map中的数据
        Iterator<String> keyIterator = userMap.keySet().iterator();
        while(keyIterator.hasNext()){
            String key = keyIterator.next();
            Integer value = userMap.get(key);
            //key-tel, value-id
            jedis.hset("ct_user", key, value+"");
        }

        //构建一个迭代器接收Map中的数据
        keyIterator = dateMap.keySet().iterator();
        while(keyIterator.hasNext()){
            String key = keyIterator.next();
            Integer value = dateMap.get(key);
            //key-tel, value-id
            jedis.hset("ct_date", key, value+"");
        }




    }
}
