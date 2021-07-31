package com.abner.ct.web.service.impl;

import com.abner.ct.web.bean.AllCalllogAvg;
import com.abner.ct.web.bean.Calllog;
import com.abner.ct.web.bean.CtUser;
import com.abner.ct.web.dao.CalllogDao;
import com.abner.ct.web.dao.CtUserDao;
import com.abner.ct.web.service.CalllogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通话日志服务对象
 */
@Service
public class CalllogServiceImpl implements CalllogService {

    @Autowired
    private CalllogDao calllogDao;
    @Autowired
    private CtUserDao ctUserDao;

    /**
     * 查询用户指定时间的通话统计信息
     * @param tel
     * @param month
     * @return
     */
    @Override
    public List<Calllog> queryMonthDatas(String tel, String month) {

        Map<String,Object> paramMap = new HashMap<String, Object>();

        paramMap.put("tel",tel);

        paramMap.put("month",month);

        List<Calllog> calllogs = calllogDao.queryMonthDatas(paramMap);

        return calllogs;
    }

    @Override
    public List<Calllog> queryYearDatas(String tel) {

        Map<String,Object> paramMap = new HashMap<String, Object>();

        paramMap.put("tel",tel);

        List<Calllog> calllogs = calllogDao.queryYearDatas(paramMap);

        return calllogs;
    }

    @Override
    public List<Calllog> querMaxUser() {
        return calllogDao.querMaxUser();
    }

    @Override
    public Integer userCount() {

        Integer userCount = calllogDao.userCount();

        return userCount;
    }

    @Override
    public List<AllCalllogAvg> AvgCalllog() {

        List<AllCalllogAvg> allCalllogAvgs = calllogDao.AvgCalllog();
        return allCalllogAvgs;
    }


}
