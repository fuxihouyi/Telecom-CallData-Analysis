package com.atguigu.ct.analysis.mapper;

import com.atguigu.ct.analysis.kv.AnalysisKey;
import com.atguigu.ct.analysis.kv.AnalysisValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * 分析数据的Mapper
 */
public class AnalysisBeanMapper extends TableMapper<AnalysisKey, Text> {

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

        //抽取数据
        String rowkey = Bytes.toString(key.get());
        String[] values = rowkey.split("_");

        String call1 = values[1];
        String call2 = values[3];
        String callTime = values[2];
        String duration = values[4];

        String year = callTime.substring(0, 4);
        String month = callTime.substring(0, 6);
        String date = callTime.substring(0, 8);


        //主叫用户-年
        context.write(new AnalysisKey(call1, year), new Text(duration));
        //主叫用户-月
        context.write(new AnalysisKey(call1, month), new Text(duration));
        //主叫用户-日
        context.write(new AnalysisKey(call1, date),  new Text(duration));

        //被叫用户-年
        context.write(new AnalysisKey(call2, year),  new Text(duration));
        //被叫用户-月
        context.write(new AnalysisKey(call2, month), new Text(duration));
        //被叫用户-日
        context.write(new AnalysisKey(call2, date),  new Text(duration));

        //用户亲密关系
//        context.write(new AnalysisKey(call2, year, call1), new Text(duration));

    }
}
