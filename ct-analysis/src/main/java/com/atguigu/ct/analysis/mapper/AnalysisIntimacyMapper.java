package com.atguigu.ct.analysis.mapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class AnalysisIntimacyMapper extends TableMapper<Text, Text> {
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

        //抽取数据
        String rowkey = Bytes.toString(key.get());
        String[] values = rowkey.split("_");

        String contact_id1 = values[1];
        String contact_id2 = values[3];
        String callTime = values[2];
        String call_duration_count = values[4];
        String year = callTime.substring(0, 4);

        //写出
        context.write(new Text(year+"_"+contact_id1+"_"+contact_id2), new Text(call_duration_count));

    }
}
