package com.abner.ct.web.dao;

import com.abner.ct.web.bean.CtUser;

import java.util.List;

public interface CtUserDao {

        List<CtUser> findAll();

        CtUser findByTelid(Integer telid);

        CtUser findByTel(String tel);

}
