package com.abner.ct.web.service;

import com.abner.ct.web.bean.AllCalllogAvg;
import com.abner.ct.web.bean.Calllog;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CalllogService {

    List<Calllog> queryMonthDatas(String tel, String calltime);

    List<AllCalllogAvg> AvgCalllog();

    List<Calllog> queryYearDatas(String tel);

    Integer userCount();

    List<Calllog> querMaxUser();
}
