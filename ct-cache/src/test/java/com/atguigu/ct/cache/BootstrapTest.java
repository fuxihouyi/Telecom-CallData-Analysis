package com.atguigu.ct.cache;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

public class BootstrapTest {

    @Test
    public void test01(){
        Jedis jedis = new Jedis("master", 6379);
        String tel = "197438116";
        String date = "2018224";
        int telid;
        String stelid = jedis.hget("ct_user", tel);
        if (stelid == null || "".equals(stelid)){
            telid = 0;
        }else{
            telid = Integer.valueOf(stelid);
        }
        int dateid;
        String sdateid = jedis.hget("ct_date", date);
        if (sdateid == null || "".equals(sdateid)){
            dateid = 0;
        }else{
            dateid = Integer.valueOf(sdateid);
        }
        System.out.println(telid);
        System.out.println(dateid);
    }

}