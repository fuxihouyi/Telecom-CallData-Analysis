package com.atguigu.ct.analysis.reducer;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 分析数据的Reduer
 */
public class AnalysisTextReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int sumCall = 0;
        int sumDuration = 0;

        for (Text value : values){
            sumCall += 1;
            sumDuration += Integer.parseInt(value.toString());
        }

        context.write(key, new Text(sumCall + "_" + sumDuration));
    }
}
