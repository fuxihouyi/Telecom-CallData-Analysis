package com.abner.ct.web.dao;

import com.abner.ct.web.bean.AllCalllogAvg;
import com.abner.ct.web.bean.Calllog;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 通话日志数据访问对象
 */
public interface CalllogDao {

    List<Calllog> queryMonthDatas(Map<String, Object> paramMap);

    List<Calllog> findByUserId(Integer id);

    List<AllCalllogAvg> AvgCalllog();

    List<Calllog> queryYearDatas(Map<String, Object> paramMap);

    Integer userCount();
    @Select("select * from ct_call,ct_user where dateid=1 AND telid = ct_user.id ORDER BY sumduration+0 DESC,sumcall+0 DESC")
    List<Calllog> querMaxUser();
}
