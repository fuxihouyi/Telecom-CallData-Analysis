package com.atguigu.ct.consumer.bean;

import com.atguigu.ct.common.bean.Consumer;
import com.atguigu.ct.common.constant.Names;
import com.atguigu.ct.consumer.dao.HBaseDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 *通话日志消费者对象
 */
public class CalllogConsumer implements Consumer {
    /**
     * 消费数据
     */
    public void consume() {

        try {
            //创建配置对象
            Properties prop = new Properties();

            //载入配置文件
//            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("consumer.properties"));
            prop.load(CalllogConsumer.class.getClassLoader().getResourceAsStream("consumer.properties"));


            //获取flume采集的数据
            KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(prop);

            //关注Kafka-Zookeeper的主题Topic
            consumer.subscribe(Arrays.asList(Names.TOPIC.getValue()));

            //构建HBase数据对象
            HBaseDao dao = new HBaseDao();

            //初始化
            dao.init();

            //消费数据
            while(true){
                //拉取数据的时间间隔控制
                ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
                //迭代获取的数据并打印出来
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println(consumerRecord.value());
                    dao.insertData(consumerRecord.value());
                    //Calllog log = new Calllog(consumerRecord.value());
                    //dao.insertData(log);
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭资源
     * @throws IOException
     */
    public void close() throws IOException {

    }
}
