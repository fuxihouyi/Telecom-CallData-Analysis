package com.abner.ct.web.controller;

import com.abner.ct.web.bean.AllCalllogAvg;
import com.abner.ct.web.bean.Calllog;
import com.abner.ct.web.bean.CtUser;
import com.abner.ct.web.bean.Intimacy;
import com.abner.ct.web.service.CalllogService;
import com.abner.ct.web.service.CtUserService;
import com.abner.ct.web.service.IntimacyService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.abner.ct.web.Util.PhoneUtil.getCarrier;
import static com.abner.ct.web.Util.PhoneUtil.getGeo;


/**
 * 通话日志控制器对象
 */
@Controller
public class CalllogController {

    @Autowired
    private CalllogService calllogService;
    @Autowired
    private CtUserService ctUserService;

    @Autowired
    private IntimacyService intimacyService;

    /**
     * 主页面
     * @param model
     * @return
     */
    @RequestMapping("/query")
    public String query(Model model){

        List<AllCalllogAvg> allCalllogAvgs = calllogService.AvgCalllog();
        List<Calllog> calllogs = calllogService.querMaxUser();
        Calllog calllog = calllogs.get(0);
        System.out.println(calllog);
        int Totaldura = 0;
        int Totalcall = 0;
        for (AllCalllogAvg allCalllogAvg : allCalllogAvgs) {
            Totaldura = (int) (Totaldura + allCalllogAvg.getAvg_dura());
            Totalcall = (int) (Totalcall + allCalllogAvg.getAvg_call());
        }
        int userCount = calllogService.userCount();
        model.addAttribute("avglogs",allCalllogAvgs);
        model.addAttribute("totaldura",Totaldura);
        model.addAttribute("totalcall",Totalcall);
        model.addAttribute("usercount",userCount);
        model.addAttribute("calllog",calllog);

        return "query";
    }

    /**
     * 某用户每月通话次数
     * @param tel
     * @param model
     * @return
     */
    @RequestMapping("/query1")
    public String query1(String tel, Model model, HttpSession session){
        Long s = System.currentTimeMillis();
        String geo = getGeo(tel);
        String carrier = getCarrier(tel);
        if( geo.length() == 0){
            geo = "未知归属地";

        }
        CtUser user = ctUserService.findByTel(tel);
        if(user == null){
            return "error";
        }else {
            List<Calllog> logs =  calllogService.queryYearDatas(tel);
            String logsJson = JSON.toJSONString(logs);
            System.out.println(logs.size());
            int sumcall = 0;
            int sumdura = 0;
            for (Calllog log : logs) {
                sumcall = sumcall + log.getSumcall();
                sumdura = sumdura + log.getSumduration();
            }
            session.setAttribute("monthlist",logsJson);
            session.setAttribute("geo",geo);
            model.addAttribute("calllogs",logsJson);
            model.addAttribute("name",user.getName());
            model.addAttribute("tel",tel);
            model.addAttribute("sumcall",sumcall);
            model.addAttribute("sumdura",sumdura);

            return "query1";
        }

    }


    /**
     * 某用户每日通话次数
     * @param tel
     * @param month
     * @param model
     * @return
     */
    @RequestMapping("/query1_1")
    public String query11(String tel, String month,Model model){

        //查询统计结果
        CtUser user = ctUserService.findByTel(tel);
        if( user == null){
            return "error";
        }else {
            List<Calllog> logs =  calllogService.queryMonthDatas(tel,month);
            String logsJson = JSON.toJSONString(logs);

            System.out.println(logs.size());

            int sumcall = 0;
            int sumdura = 0;
            for (Calllog log : logs) {
                sumcall = sumcall + log.getSumcall();
                sumdura = sumdura + log.getSumduration();
            }
            model.addAttribute("calllogs",logsJson);
            model.addAttribute("name",user.getName());
            model.addAttribute("tel",tel);
            model.addAttribute("sumcall",sumcall);
            model.addAttribute("sumdura",sumdura);
            return "query11";
        }
    }

    /**
     * 某用户每月通话时长
     * @param tel
     * @param model
     * @return
     */
    @RequestMapping("/query2")
    public String query2(String tel, Model model,HttpSession session){

            Long s = System.currentTimeMillis();
            String geo = getGeo(tel);
            String carrier = getCarrier(tel);
            if( geo.length() == 0){
                geo = "未知归属地";

            }
            session.setAttribute("geo",geo);


        CtUser user = ctUserService.findByTel(tel);
        if(user == null){
            return "error";
        }else {
            List<Calllog> logs =  calllogService.queryYearDatas(tel);
            String logsJson = JSON.toJSONString(logs);
            System.out.println(logs.size());
            int sumcall = 0;
            int sumdura = 0;
            for (Calllog log : logs) {
                sumcall = sumcall + log.getSumcall();
                sumdura = sumdura + log.getSumduration();
            }
            session.setAttribute("monthlist",logsJson);
            model.addAttribute("calllogs",logsJson);
            model.addAttribute("name",user.getName());
            model.addAttribute("tel",tel);
            model.addAttribute("sumcall",sumcall);
            model.addAttribute("sumdura",sumdura);

            return "query2";
        }

    }


    /**
     * 某用户每日通话时长
     * @param tel
     * @param month
     * @param model
     * @return
     */
    @RequestMapping("/query2_1")
    public String query21(String tel,String month, Model model){

        CtUser user = ctUserService.findByTel(tel);
        if(user == null){
            return "error";
        }else {
            List<Calllog> logs =  calllogService.queryMonthDatas(tel,month);
            String logsJson = JSON.toJSONString(logs);
            System.out.println(logs.size());
            int sumcall = 0;
            int sumdura = 0;
            for (Calllog log : logs) {
                sumcall = sumcall + log.getSumcall();
                sumdura = sumdura + log.getSumduration();
            }
            model.addAttribute("calllogs",logsJson);
            model.addAttribute("name",user.getName());
            model.addAttribute("tel",tel);
            model.addAttribute("sumcall",sumcall);
            model.addAttribute("sumdura",sumdura);

            return "query21";
        }

    }

    /**
     * 用户亲密度
     * @param tel
     * @param model
     * @return
     */
    @RequestMapping("/query3")
    public String intimacy( String tel, Model model,HttpSession session) {

            Long s = System.currentTimeMillis();
            String geo = getGeo(tel);
            String carrier = getCarrier(tel);
            if( geo.length() == 0){
                geo = "未知归属地";

            }
            session.setAttribute("geo",geo);

        // 查询统计结果 ： Mysql
        CtUser user = ctUserService.findByTel(tel);
        if(user == null){
            return "error";
        }else {
            List<Intimacy> intimacyList = intimacyService.queryIntimacy(tel);
            CtUser ctUser = intimacyService.queryBytel(tel);
            List<Calllog> logs =  calllogService.queryYearDatas(tel);
            String logsJson = JSON.toJSONString(logs);
            System.out.println(logs.size());
            int sumcall = 0;
            int sumdura = 0;
            for (Calllog log : logs) {
                sumcall = sumcall + log.getSumcall();
                sumdura = sumdura + log.getSumduration();
            }
            model.addAttribute("intimacyList", JSON.toJSON(intimacyList));
            model.addAttribute("ctuser", ctUser);
            model.addAttribute("sumcall",sumcall);
            model.addAttribute("sumdura",sumdura);
            return "query3";
        }

    }

}
