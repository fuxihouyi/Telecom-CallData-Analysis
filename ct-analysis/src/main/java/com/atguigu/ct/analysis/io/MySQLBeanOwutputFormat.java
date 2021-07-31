package com.atguigu.ct.analysis.io;

import com.atguigu.ct.analysis.kv.AnalysisKey;
import com.atguigu.ct.analysis.kv.AnalysisValue;
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
public class MySQLBeanOwutputFormat extends OutputFormat<AnalysisKey, AnalysisValue> {

    protected static class MySQLRecordWriter extends RecordWriter<AnalysisKey, AnalysisValue> {

        //声明一个mysql连接类
        private Connection connection = null;

        //声明一个jedis连接类
        private Jedis jedis = null;

        //通过-构造方法获取资源
        public MySQLRecordWriter(){
            //获取MySQL操作源
            connection = JDBCUtil.getConnection();

            //获取redis来凝结
            jedis = new Jedis("master", 6379);

        }

        /**
         * 输出数据
         * @param key
         * @param value
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void write(AnalysisKey key, AnalysisValue value) throws IOException, InterruptedException {
            PreparedStatement pstat = null;
            try {
                String insertSQL = "insert into ct_call (telid, dateid, sumcall, sumduration) value (?, ?, ?, ?)";
                pstat = connection.prepareStatement(insertSQL);

                //获取主叫用户id
                int telid;
                String stelid = jedis.hget("ct_user", key.getTel());
                if (stelid == null || "".equals(stelid)){
                    telid = 0;
                }else{
                    telid = Integer.valueOf(stelid);
                }

                //获取拨号时间id
                int dateid;
                String sdateid = jedis.hget("ct_date", key.getDate());
                try{
                    jedis.hget("ct_date", key.getDate());
                }catch (Exception e){

                }

                if (sdateid == null || "".equals(sdateid)){
                    dateid = 0;
                }else{
                    dateid = Integer.valueOf(sdateid);
                }
                pstat.setInt(1, telid);
                pstat.setInt(2, dateid);
                pstat.setString(3, value.getSumCall());
                pstat.setString(4, value.getSumDuration());
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
    public RecordWriter<AnalysisKey, AnalysisValue> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
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
