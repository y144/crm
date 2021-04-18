package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * 杨廷甲
 * 2020-12-24
 */

@Controller
public class DicTypeController {

    @Autowired
    private DicTypeService dicTypeService;

    //通过查询全部的字典类型，将字典类型展现的页面
    @RequestMapping("/settings/dictionary/type/index.do")
    public String dicTypeIndex(Model model){
        //调用service层方法查询所有的数据字典类型列表
        List<DicType> dicTypeList = dicTypeService.selectAllDicType();
        model.addAttribute("dicTypeList", dicTypeList);
        return "settings/dictionary/type/index";
    }

    //跳转到保存字典类型的页面
    @RequestMapping("/settings/dictionary/type/toSave.do")
    public String toSave(){
        //要保存先请求转发跳转的填写保存信息的页面上，使用请求转发
        return "settings/dictionary/type/save";
    }

    //在保存的时候，这时候就要创建一个返回的结果集，将得到的结果，传给返回结果集的这个对象，在将此对象返回给页面，在页面上一定是通过ajax方式访问的，所以将结果集返回至ajax的数据接收处进行处理
    @RequestMapping("settings/dictionary/type/saveCreateDicType.do")
    public @ResponseBody Object saveCreatDicType(DicType dicType){
        ReturnObject returnObject = new ReturnObject();
        try {
            int dicTypeNum = dicTypeService.saveDicType(dicType);
            if (dicTypeNum > 0){
                //保存成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                //保存失败
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //保存失败
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败！！");
        }
        return returnObject;
    }

    //在保存字典类型时，首先要对保存的字典类型进行判断是否已经存在
    @RequestMapping("settings/dictionary/type/checkCode.do")
    public @ResponseBody Object checkCode(String code){
        //调用service层查询这个即将要保存的字段编码类型，看是否重复已经存在
        DicType dicType = dicTypeService.selectDicTypeById(code);
        //创建结果集对象，对返回结果进行保存
        ReturnObject returnObject = new ReturnObject();
        if (dicType == null){
            //表示根据输入的这类字典类型的编码，没有找到的字典类型，就与数据库中的字典类型不会重复就是可用的
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            //表示该编码类型在数据库中已经存在，则重复，不可用
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("该编码已存在，请重新输入");
        }
        return returnObject;
    }

    //在更新数据前，先要把需要更新的这一条数据找出来显示在页面上
    @RequestMapping("settings/dictionary/type/editDicType.do")
    public String updateSelectDicType(String code,Model model){
        DicType dicType = dicTypeService.selectDicTypeById(code);
        //把数据保存在request中
        model.addAttribute("dicType", dicType);
        //通过请求转发跳转页面
        return "settings/dictionary/type/edit";
    }

    //找出来这条数据显示在页面上后，对这条数据进行修改，然后进行保存 在页面上是使用Ajax发送的异步请求
    @RequestMapping("settings/dictionary/type/saveEditDicType.do")
    public @ResponseBody Object updateDicType(DicType dicType){
        ReturnObject returnObject = new ReturnObject();
        try {
            int updateDicType = dicTypeService.updateDicType(dicType);
            if (updateDicType > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("更新成功！！");
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

    //批量删除
    @RequestMapping("/settings/dictionary/type/deleteDicTypeByCodes.do")
    public @ResponseBody Object deleteDicType(String[] code){//这里的这个参数名，要与后台ajax提交数据中的“codesStr+="code="+this.value+"&";//相当于codesStr=codesStr+"code="+this.value+"&";”一致
        ReturnObject returnObject = new ReturnObject();
        try {
            int deleteDic = dicTypeService.deleteDicTypeBatch(code);
            if (deleteDic > 0){
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
