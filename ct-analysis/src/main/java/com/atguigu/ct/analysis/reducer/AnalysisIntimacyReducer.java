package com.atguigu.ct.analysis.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AnalysisIntimacyReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        int call_count = 0;
        int call_duration_count = 0;

        for(Text value : values){
            call_count += 1;
            call_duration_count += Integer.parseInt(value.toString());
        }

        context.write(key, new Text(call_count+"_"+call_duration_count));


    }
}
