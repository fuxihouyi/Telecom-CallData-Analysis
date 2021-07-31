package com.abner.ct.web.dao;

import com.abner.ct.web.bean.CtUser;
import com.abner.ct.web.bean.Intimacy;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IntimacyDao {
    List<Intimacy> queryIntimacy(String tel);
    @Select("select * from ct_user where tel=#{tel}")
    CtUser queryBytel(String tel);
}
