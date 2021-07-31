package com.atguigu.ct.consumer;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import scala.util.control.Exception;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BootstrapTest {

    @Test
    public void test01(){
        List<byte[]> bsList = new ArrayList<byte[]>();
        byte[][] bs = new byte[5][];
        byte bt = 'a';       //ASCLL
        System.out.println("bt:"+bt);
        System.out.println("----------------");
        byte[] bts = Bytes.toBytes("a");
        System.out.println("bts(string):"+Bytes.toString(bts)+"\nbts(hex):"+ Bytes.toHex(bts));
        System.out.println("----------------");



        System.out.println("bsList-empty:"+bsList);
        System.out.println("----------------");
        System.out.println("bs-empty:"+bs[1]);
        System.out.println("----------------");


        for (int i=0; i<5; i++){
            String is = String.valueOf(i);
//            String is = String.valueOf(i)+"|";
            bsList.add(Bytes.toBytes(is));
        }
        System.out.println("bsList-before-toArray:"+bsList);
        System.out.println("----------------");
        System.out.println("bs-before-toArray:"+bs[1]);
        System.out.println("----------------");

        bsList.toArray(bs);

        System.out.println("bs-after-toArray:"+bs[1][0]);
        System.out.println("----------------");
        for (byte [] s :bsList){
            System.out.println("bsList-before-toArray-----:"+s);
        }

    }

    @Test
    public void test02(){
        //13301234567
        String tel = "13301234567";
        String date = "20180101000000";
        String usercode = tel.substring(tel.length()-4);
        System.out.println("usercodde:" + usercode);

        //20180101000000
        String yearMonth = date.substring(0, 6);
        System.out.println("date:" + yearMonth);
    }


}