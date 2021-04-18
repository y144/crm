package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 杨廷甲
 * 2020-12-31
 */
@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    //点击name 进入本条市场活动详情
    @RequestMapping("/workbench/activity/detailActivity")
    public String detailActivity(String id, Model model){
        //查新市场活动明细
        Activity activity = activityService.queryActivityForDetailById(id);
        //查询备注
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        //把数据保存在model中
        model.addAttribute("activity", activity);
        model.addAttribute("remarkList", remarkList);
        return "workbench/activity/detail";
    }

    //新增备注
    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    public @ResponseBody Object saveCreateActivityRemark(ActivityRemark activityRemark, HttpSession session){

        //获得登陆者的信息就是发表备注的人的信息
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        //封装数据
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateBy(user.getId());
        activityRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        activityRemark.setEditFlag("0");

        //设置返回集合
        ReturnObject returnObject = new ReturnObject();

        try {
            //调用service层保存市场备注
            int remark = activityRemarkService.saveActivityRemark(activityRemark);

            if (remark > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //这里有数据的返回 保存完得显示在页面
                returnObject.setRetData(remark);
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

    //更新备注
    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    public @ResponseBody Object updateActivityRemark(ActivityRemark activityRemark,HttpSession session){

        //封装参数
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        activityRemark.setEditFlag("1");
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        //创建返回集
        ReturnObject returnObject = new ReturnObject();

        try {
            //调用service层 保存数据
            int remark = activityRemarkService.updateActivityRemark(activityRemark);
            if (remark > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更新失败，请稍后重试!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败，请稍后重试!");
        }
        return returnObject;
    }

    //删除备注
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    public @ResponseBody Object deleteActivityRemark(String id){
        ReturnObject returnObject = new ReturnObject();

        try {
            int remark = activityRemarkService.deleteActivityRemarkById(id);
            if (remark > 0){
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
}
