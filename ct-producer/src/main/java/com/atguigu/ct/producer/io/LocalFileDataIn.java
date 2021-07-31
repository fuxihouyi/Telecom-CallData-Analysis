package com.atguigu.ct.producer.io;

import com.atguigu.ct.common.bean.Data;
import com.atguigu.ct.common.bean.DataIn;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地文件的输入
 */
public class LocalFileDataIn implements DataIn {

    private BufferedReader reader = null;

    public LocalFileDataIn(String path) {
        setPath(path);
    }

    public void setPath(String path) {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Object read() throws IOException {
        return null;
    }

    /**
     * 读取数据，返回数据集合
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T extends Data> List<T> read(Class<T> clazz) throws IOException {
        List<T> ts = new ArrayList<T>();
        try {
            //从数据文件中读取所有的数据
            String line = null;
            while((line = reader.readLine()) != null){
                T t = (T) clazz.newInstance();
                t.setValue(line);
                ts.add(t);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //将数据转换为指定类型的对象，封装成为集合返回
        return ts;
    }

    /**
     * 关闭资源
     * @throws IOException
     */
    public void close() throws IOException {
        if (reader != null ){
            reader.close();
        }
    }

}
