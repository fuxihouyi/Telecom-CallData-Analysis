package com.atguigu.ct.analysis.io;

import com.atguigu.ct.common.util.JDBCUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MySQL的数据格式化输入对象
 */
public class AnalysisIntimacyMySQLTextOwutputFormat extends OutputFormat<Text, Text> {

    protected static class MySQLRecordWriter extends RecordWriter<Text, Text> {

        //声明一个mysql连接类
        private Connection connection = null;


        //通过-构造方法获取资源
        public MySQLRecordWriter(){
            //获取MySQL操作源
            connection = JDBCUtil.getConnection();

        }

        /**
         * 输出数据
         * @param key
         * @param value
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void write(Text key, Text value) throws IOException, InterruptedException {
            /**keys array info
             * 0. year
             * 1. contact_id1
             * 2. contact_id2
             */
            String [] keys = key.toString().split("_");

            /**
             * values array info
             * 0. call_count
             * 1. call_duration_count
             */
            String [] values = value.toString().split("_");
            String sumCall = values[0];
            String sumDuration = values[1];
            PreparedStatement pstat = null;
            try {
                String insertSQL = "insert into tb_intimacy (contact_id1, contact_id2, call_count, call_duration_count) value (?, ?, ?, ?)";
                pstat = connection.prepareStatement(insertSQL);

//                //获取主叫id
//                Long contact_id1 = Long.parseLong();
//
//                //获取被叫id
//                Long contact_id2 = Long.parseLong();

                //获取年通话总次数
                Long call_count = Long.parseLong(values[0]);

                //获取年通话总时间
                Long call_duration_count = Long.parseLong(values[1]);


                pstat.setString(1, keys[1]);

                pstat.setString(2, keys[2]);

                pstat.setLong(3, call_count);

                pstat.setLong(4, call_duration_count);

//                pstat.setLong(1, );
//
//                pstat.setLong(2, 2);
//
//                pstat.setLong(3, 3);
//
//                pstat.setLong(4, 4);

                pstat.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (pstat != null){
                    try {
                        pstat.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 释放资源
         * @param taskAttemptContext
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MySQLRecordWriter();
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {

    }


    private FileOutputCommitter committer = null;

    public static Path getOutputPath(JobContext job) {
        String name = job.getConfiguration().get("mapreduce.output.fileoutputformat.outputdir");
        return name == null ? null : new Path(name);
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
        if (this.committer == null) {
            Path output = getOutputPath(context);
            this.committer = new FileOutputCommitter(output, context);
        }

        return this.committer;
    }
}
