package com.atguigu.ct.common.constant;

import javax.security.auth.login.Configuration;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigConstant {
    private static Map<String, String> valueMap = new HashMap<String, String>();

    static{
        //国际化-不同环境布局不同-风格不同-内容文字不同
        ResourceBundle ct = ResourceBundle.getBundle("ct");
        Enumeration<String> enums = ct.getKeys();
        while(enums.hasMoreElements()){
            String key = enums.nextElement();
            String value = ct.getString(key);
            valueMap.put(key, value);
        }
    }

    public static String getVal(String key){
        return valueMap.get(key);
    }

    public static void main(String[] args) {
        System.out.println(ConfigConstant.getVal("ct.cf.info"));
    }

}
