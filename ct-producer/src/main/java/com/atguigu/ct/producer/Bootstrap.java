package com.atguigu.ct.producer;

import com.atguigu.ct.common.bean.Producer;
import com.atguigu.ct.producer.bean.LocalFileProducer;
import com.atguigu.ct.producer.io.LocalFileDataIn;
import com.atguigu.ct.producer.io.LocalFileDataOut;

import java.io.IOException;

/**
 * 启动对象
 */
public class Bootstrap {
    public static void main(String[] args) throws IOException {

        //判断系统是否传入参数
        if (args.length < 2){
            System.out.println("请输入2个参数：1. 数据源路径 2.输出数据路径");
            System.exit(1);
        }

        //构建生产者对象
        Producer producer = new LocalFileProducer();

        //注册输入
        producer.setIn(new LocalFileDataIn("C:\\Users\\Administrator\\Desktop\\Data_Analysis\\atguigu-project-ct\\ct-common\\src\\main\\resources\\contact.log"));
        producer.setIn(new LocalFileDataIn(args[0]));

        //注册输出
//        producer.setOut(new LocalFileDataOut("C:\\Users\\Administrator\\Desktop\\Data_Analysis\\atguigu-project-ct\\ct-common\\src\\main\\resources\\call.log"));
        producer.setOut(new LocalFileDataOut(args[1]));

        //生产数据
        producer.produce();

        //关闭生产者对象
        producer.close();
    }
}
