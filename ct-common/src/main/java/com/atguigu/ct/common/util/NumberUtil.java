package com.atguigu.ct.common.util;

import java.text.DecimalFormat;

public class NumberUtil {
    /**
     * 将数字格式化为指定长度字符串
     * @param num
     * @param length
     * @return
     */
    public static String format(int num, int length){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i<=length; i++){
            stringBuffer.append("0");
        }
        DecimalFormat df = new DecimalFormat(stringBuffer.toString());
        return df.format(num);
    }

//    public static void main(String[] args) {
//        System.out.println(format(10, 10));
//    }
}
