package com.atguigu.ct.producer.bean;

import com.atguigu.ct.common.bean.DataIn;
import com.atguigu.ct.common.bean.DataOut;
import com.atguigu.ct.common.bean.Producer;
import com.atguigu.ct.common.util.DateUtil;
import com.atguigu.ct.common.util.NumberUtil;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 本地数据文件生产者
 */
public class LocalFileProducer implements Producer {

    private DataIn in;

    private DataOut out;

    private volatile boolean flg=true;   //volatile：增强内存可见性，类似于静态变量的效果

    public void setIn(DataIn in) {
        this.in=in;
    }

    public void setOut(DataOut out) {
        this.out=out;
    }

    /**
     * 生产数据
     */
    public void produce() throws IOException {
            try{
                //读取通讯录数据
                List<Contact> contacts = in.read(Contact.class);
//                for (Contact contact : contacts){
//                    System.out.println(contact.toString());
//                }

                while (flg){
                    //从通讯录中随机查找两个电话号码（主叫，被叫）
                    int call1Index = new Random().nextInt(contacts.size());
                    int call2Index = 0;
                    while (true){
                        call2Index = new Random().nextInt(contacts.size());
                        if (call1Index != call2Index){
                            break;
                        }
                    }
                    Contact call1 = contacts.get(call1Index);
                    Contact call2 = contacts.get(call2Index);

                    //随机生成的通话时间
                    String startDate = "20180101000000";
                    String endDate = "20190101000000";
                    long startTime = DateUtil.parse(startDate, "yyyyMMddHHmmss").getTime();
                    long endTime = DateUtil.parse(endDate, "yyyyMMddHHmmss").getTime();
                    long callTime = startTime+(long)((endTime - startTime)*Math.random());
                    //通话开始时间字符串
                    String callTimeString = DateUtil.format(new Date(callTime), "yyyyMMddHHmmss");


                    //随机生成的通话时长
                    String duration = NumberUtil.format(new Random().nextInt(3600), 4);

                    //生成通话记录
                    Calllog log = new Calllog(call1.getTel(), call2.getTel(), callTimeString, duration);

                    System.out.println(log);

                    //将通话记录刷写到数据文件中
                    out.write(log);


                    Thread.sleep(500);

                }

            }catch (Exception e){

            }

    }

    /**
     * 关闭生产者
     * @throws IOException
     */
    public void close() throws IOException {
        if (in != null){
            in.close();
        }

        if (out != null){
            out.close();
        }
    }
}
