package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * 杨廷甲
 * 2020-12-25
 */
public interface DicValueService {

    //查询全部的字典值
    List<DicValue> selectAllDicVal();

    //创建保存字典值
    int saveDicValue(DicValue dicValue);

    //更新字典值
    int updateDicValue(DicValue dicValue);

    //按主键查找字典值
    DicValue selectDicValueById(String id);

    //批量删除
    int deleteDicValueBatch(String[] id);

    List<DicValue> selectDicValByTypeCode(String typeCode);

    //在监听器中使用缓存，在Tomcat启动时进行，将数据放在缓存 拿到了所有的字典值 不建议使用
    Map<String,List<DicValue>> getAll();


}
