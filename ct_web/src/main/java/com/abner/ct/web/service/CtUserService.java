package com.abner.ct.web.service;

import com.abner.ct.web.bean.CtUser;

import java.util.List;

public interface CtUserService {

    List<CtUser> findAll();

    CtUser findByTel(String tel);

}
