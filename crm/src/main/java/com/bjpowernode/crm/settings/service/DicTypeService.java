package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicType;

import java.util.List;

/**
 * 杨廷甲
 * 2020-12-24
 */
public interface DicTypeService {

    //查询全部的字典类型
    List<DicType> selectAllDicType();

    //按主键进行查找字典类型
    DicType selectDicTypeById(String code);

    //保存添加字典类型
    int saveDicType(DicType dicType);

    //批量删除字典类型
    int deleteDicTypeBatch(String[] codes);

    //更新字典类型
    int updateDicType(DicType dicType);
}
