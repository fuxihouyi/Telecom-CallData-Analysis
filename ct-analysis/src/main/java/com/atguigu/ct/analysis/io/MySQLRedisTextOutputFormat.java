package com.atguigu.ct.analysis.io;

import com.atguigu.ct.common.util.JDBCUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLRedisTextOutputFormat extends OutputFormat<Text, Text> {

    protected static class MySQLRecordWriter extends RecordWriter<Text, Text> {

        //声明一个mysql连接类
        private Connection connection = null;

        //声明一个jedis连接类
        private Jedis jedis = null;

        //通过-构造方法获取资源
        public MySQLRecordWriter(){
            //获取MySQL操作源
            connection = JDBCUtil.getConnection();

            //获取redis来凝结
            jedis = new Jedis("192.168.1.4", 6379);

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
            String [] values = value.toString().split("_");
            String sumCall = values[0];
            String sumDuration = values[1];
            PreparedStatement pstat = null;

            try {
                String insertSQL = "insert into ct_call (telid, dateid, sumcall, sumduration) value (?, ?, ?, ?)";
                pstat = connection.prepareStatement(insertSQL);

                //分解reduce输出的key
                String [] keys = key.toString().split("_");

                //获取号码
                String tel = keys[0];

                //获取日期
                String date = keys[1];

                //获取主叫用户id
                int telid;
                String stelid = jedis.hget("ct_user", tel);
                if (stelid == null || "".equals(stelid)){
                    telid = 0;
                }else{
                    telid = Integer.valueOf(stelid);
                }

                //获取拨号时间
                int dateid;
                String sdateid = jedis.hget("ct_date", date);
                if (sdateid == null || "".equals(sdateid)){
                    dateid = 0;
                }else{
                    dateid = Integer.valueOf(sdateid);
                }
                pstat.setInt(1, telid);
                pstat.setInt(2, dateid);
                pstat.setString(3, sumCall);
                pstat.setString(4, sumDuration);
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

            if (jedis != null){
                jedis.close();
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
        String name = job.getConfiguration().get(FileOutputFormat.OUTDIR);
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
