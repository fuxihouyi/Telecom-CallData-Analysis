package com.abner.ct.web.service.impl;

import com.abner.ct.web.bean.Calllog;
import com.abner.ct.web.bean.CtUser;
import com.abner.ct.web.dao.CalllogDao;
import com.abner.ct.web.dao.CtUserDao;
import com.abner.ct.web.service.CtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CtUserServiceImpl implements CtUserService {

    @Autowired
    private CtUserDao ctUserDao;
    @Autowired
    private CalllogDao calllogDao;

    @Override
    public List<CtUser> findAll() {

        List<CtUser> ctUserList = ctUserDao.findAll();
        for (CtUser ctUser : ctUserList) {
            Integer ctUserId = ctUser.getId();
            List<Calllog> calllogs = calllogDao.findByUserId(ctUserId);
            ctUser.setCalllogs(calllogs);
        }

        return ctUserList;
    }

    @Override
    public CtUser findByTel(String tel) {

        CtUser ctUser = ctUserDao.findByTel(tel);

        return ctUser;
    }
}
