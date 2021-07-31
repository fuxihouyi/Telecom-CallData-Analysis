package com.abner.ct.web.service;

import com.abner.ct.web.bean.CtUser;
import com.abner.ct.web.bean.Intimacy;

import java.util.List;

public interface IntimacyService {
    List<Intimacy> queryIntimacy(String tel);
    CtUser queryBytel(String tel);
}
