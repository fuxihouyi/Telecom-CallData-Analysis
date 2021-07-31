package com.atguigu.ct.analysis;

import com.atguigu.ct.analysis.tool.AnalysisBeanTool;
import com.atguigu.ct.analysis.tool.AnalysisIntimacyTool;
import com.atguigu.ct.analysis.tool.AnalysisTextTool;
import org.apache.hadoop.util.ToolRunner;

public class AnalysisData {
    public static void main(String[] args) throws Exception {
        /**
         * 从Hbase通过Mapreduce抽取通话日志数据到Mysql表ct_call
         */
//        int result = ToolRunner.run(new AnalysisTextTool(), args);

        /**
         * 从Hbase通过Mapreduce抽取亲密度分析数据到MySql表tb_intimacy
         */
        int result = ToolRunner.run(new AnalysisIntimacyTool(), args);
    }
}
