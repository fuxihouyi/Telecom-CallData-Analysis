package com.abner.ct.web.service.impl;

import com.abner.ct.web.bean.CtUser;
import com.abner.ct.web.bean.Intimacy;
import com.abner.ct.web.dao.IntimacyDao;
import com.abner.ct.web.service.IntimacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntimacyServiceImpl implements IntimacyService {
    @Autowired
    private IntimacyDao intimacyDao;
    @Override
    public List<Intimacy> queryIntimacy(String tel) {
        return intimacyDao.queryIntimacy(tel);
    }

    @Override
    public CtUser queryBytel(String tel) {
        return intimacyDao.queryBytel(tel);
    }
}

