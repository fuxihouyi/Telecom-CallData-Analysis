package com.abner.ct.web.bean;

import java.util.List;

public class CtUser {

    private Integer id;
    private String tel;
    private String name;

    private List<Calllog> calllogs;

    public List<Calllog> getCalllogs() {
        return calllogs;
    }

    public void setCalllogs(List<Calllog> calllogs) {
        this.calllogs = calllogs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
