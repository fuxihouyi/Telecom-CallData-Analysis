package com.atguigu.ct.common.bean;

public abstract class Data implements Val {
    public String content;

    public void setValue(Object val) {
        this.content=(String)val;
    }

    public String getValue() {
        return this.content;
    }
}
