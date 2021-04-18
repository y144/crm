package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.nio.channels.NonReadableChannelException;
import java.time.Period;
import java.util.Date;

/**
 * 杨廷甲
 * 2021-01-07
 */
@Controller
public class ClueRemarkController {

    @Autowired
    private ClueRemarkService clueRemarkService;

    //增加线索评论
    @RequestMapping("/workbench/clue/saveCreateClueRemark.do")
    public @ResponseBody Object saveCreateClueRemark(HttpSession session,ClueRemark clueRemark){

        User user = (User) session.getAttribute(Contants.SESSION_USER);

        //封装参数
        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        clueRemark.setEditFlag("0");


        ReturnObject returnObject = new ReturnObject();

        try {
            //调用service层保存市场备注
            int remark = clueRemarkService.insertClueRemark(clueRemark);

            if (remark > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //这里有数据的返回 保存完得显示在页面
                returnObject.setRetData(clueRemark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("对不起，保存失败，请稍后重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("对不起，保存失败，请稍后重试！");
        }

        return returnObject;

    }

    //删除线索评论
    @RequestMapping("/workbench/clue/deleteClueRemarkById.do")
    public @ResponseBody Object deleteClueRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();

        try {
            int ret = clueRemarkService.deleteClueRemarkById(id);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败！请稍后重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败！请稍后重试！");
        }
        return returnObject;
    }

    //更新线索信息
    @RequestMapping("/workbench/clue/saveEditClueRemark.do")
    public @ResponseBody Object saveEditClueRemark(HttpSession session,ClueRemark clueRemark){

       User user = (User) session.getAttribute(Contants.SESSION_USER);

       //封装参数
        clueRemark.setEditBy(user.getId());
        clueRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        clueRemark.setEditFlag("1");

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service层保存市场备注
            int ret = clueRemarkService.editClueRemark(clueRemark);

            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //这里有数据的返回 保存完得显示在页面
                returnObject.setRetData(clueRemark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("对不起，保存失败，请稍后重试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("对不起，保存失败，请稍后重试！");
        }

        return returnObject;

    }
}
