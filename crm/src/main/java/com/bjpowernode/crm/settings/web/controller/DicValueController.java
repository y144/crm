package com.bjpowernode.crm.settings.web.controller;

import com.alibaba.druid.stat.TableStat;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicTypeService;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

/**
 * 杨廷甲
 * 2020-12-25
 */
@Controller
public class DicValueController {


    @Autowired
    private DicValueService dicValueService;

    //先通过点击主页面的导航 字典值标签 将页面跳转到字典值页面
    @RequestMapping("/settings/dictionary/value/index.do")
    public String indexDicVal(Model model){
        List<DicValue> dicValueList = dicValueService.selectAllDicVal();
        model.addAttribute("dicValueList", dicValueList);
        return "settings/dictionary/value/index";
    }

    @Autowired
    private DicTypeService dicTypeService;
    //点击 创建按钮 跳转到创建的页面
    @RequestMapping("/settings/dictionary/value/toSave.do")
    public String indexToSave(Model model){
        List<DicType> dicTypeList = dicTypeService.selectAllDicType();
        model.addAttribute("dicTypeList", dicTypeList);
        return "settings/dictionary/value/save";
    }



    //点击保存按钮对数据进行创建保存
    @RequestMapping("/settings/dictionary/value/saveCreateDicValue.do")
    public @ResponseBody Object saveDicValue(DicValue dicValue){

        //因为在页面上取得的参数中没有主见id，所以在控制层使用工具类自动生成一个UUID随机数，封装在字典值的主键中，作为主键，保存进数据库
        dicValue.setId(UUIDUtils.getUUID());

        ReturnObject returnObject = new ReturnObject();
        try {
            int saveDicValue = dicValueService.saveDicValue(dicValue);
            if (saveDicValue > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("字典值保存失败！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("字典值保存失败！！");
        }
        return returnObject;
    }

    //进行编辑的页面跳转
    @RequestMapping("/settings/dictionary/value/editDicValue.do")
    public String toUpdateDicValue(String id, Model model){
        DicValue dicValue = dicValueService.selectDicValueById(id);
        model.addAttribute("dicValue", dicValue);
        return "settings/dictionary/value/edit";
    }

    //跳转到编辑的页面则开始更新，将更完的信息进行提交
    @RequestMapping("/settings/dictionary/value/saveEditDicValue.do")
    public @ResponseBody Object updateDicValue(DicValue dicValue){
        ReturnObject returnObject = new ReturnObject();
        try {
            int updateDicValue = dicValueService.updateDicValue(dicValue);
            if (updateDicValue > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更新失败！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败！！");
        }
        return returnObject;
    }

    @RequestMapping("settings/dictionary/value/deleteDicValueByIds.do")
    public @ResponseBody Object deleteDicValue(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int deleteNum = dicValueService.deleteDicValueBatch(id);
            if (deleteNum > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败！！");
        }
        return returnObject;
    }
}
