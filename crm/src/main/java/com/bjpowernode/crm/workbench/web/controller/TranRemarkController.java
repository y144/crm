package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 杨廷甲
 * 2021-01-09
 */
@Controller
public class TranRemarkController {

    @Autowired
    private TranRemarkService tranRemarkService;

    @RequestMapping("/workbench/clue/saveCreateTranRemark.do")
    public @ResponseBody Object saveCreateTranRemark(HttpSession session, TranRemark tranRemark){
        User user = (User)session.getAttribute(Contants.SESSION_USER);

        tranRemark.setId(UUIDUtils.getUUID());
        tranRemark.setCreateBy(user.getId());
        tranRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranRemark.setEditFlag("0");

        ReturnObject returnObject = new ReturnObject();

        try {
            int ret = tranRemarkService.saveTranRemark(tranRemark);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(ret);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败");
        }
        return returnObject;
    }

    //更新备注
    @RequestMapping("/workbench/tran/saveEditTranRemark.do")
    public @ResponseBody Object updateActivityRemark(TranRemark tranRemark, HttpSession session){

        //封装参数
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        tranRemark.setEditFlag("1");
        tranRemark.setEditby(user.getId());
        tranRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        //创建返回集
        ReturnObject returnObject = new ReturnObject();

        try {
            //调用service层 保存数据
            int remark = tranRemarkService.editTranRemark(tranRemark);
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
    @RequestMapping("/workbench/tran/deleteTranRemarkById.do")
    public @ResponseBody Object deleteActivityRemark(String id){
        ReturnObject returnObject = new ReturnObject();

        try {
            int remark = tranRemarkService.deleteTranRemark(id);
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
