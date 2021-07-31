package com.atguigu.ct.common.bean;

import java.io.Closeable;
import java.io.IOException;

/**
 * 生产者接口
 * -Closeabel可关闭
 */
public interface Producer extends Closeable {
    //输入源
    public void setIn(DataIn in);

    //输出源
    public void setOut(DataOut out);

    //生产数据
    public void produce() throws IOException;
}
