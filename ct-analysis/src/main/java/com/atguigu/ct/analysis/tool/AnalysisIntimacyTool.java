package com.atguigu.ct.analysis.tool;


import com.atguigu.ct.analysis.io.AnalysisIntimacyMySQLTextOwutputFormat;
import com.atguigu.ct.analysis.mapper.AnalysisIntimacyMapper;
import com.atguigu.ct.analysis.reducer.AnalysisIntimacyReducer;
import com.atguigu.ct.common.constant.Names;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;


/**
 * 分析数据的工具类
 */
public class AnalysisIntimacyTool implements Tool {
    public int run(String[] strings) throws Exception {
        //构建Job任务
        Job job = Job.getInstance();

        //指定任务类
        job.setJarByClass(AnalysisIntimacyTool.class);

        //构建数据类
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(Names.CF_CALLER.getValue()));

        //mapper
        TableMapReduceUtil.initTableMapperJob(
                Names.TABLE.getValue(),
                scan,
                AnalysisIntimacyMapper.class,
                Text.class,
                Text.class,
                job,
                false
        );

        //reducer
        job.setReducerClass(AnalysisIntimacyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //outputformat
        job.setOutputFormatClass(AnalysisIntimacyMySQLTextOwutputFormat.class);

        //判断工作是否完成
        boolean flg = job.waitForCompletion(true);
        if (flg){
            return JobStatus.State.SUCCEEDED.getValue();
        }else {
            return JobStatus.State.FAILED.getValue();
        }
    }

    public void setConf(Configuration configuration) {

    }

    public Configuration getConf() {
        return null;
    }
}
