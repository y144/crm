package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicTypeMapper;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicTypeService;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 杨廷甲
 * 2020-12-25
 */

@Service
public class DicValueServiceImpl implements DicValueService {

    @Autowired
    private DicValueMapper dicValueMapper;

    //查询所有的字典值
    @Override
    public List<DicValue> selectAllDicVal() {
        return dicValueMapper.selectAllDicVal();
    }


    //保存添加字典值
    @Override
    public int saveDicValue(DicValue dicValue) {
        return dicValueMapper.saveDicVal(dicValue);
    }

    @Override
    public int updateDicValue(DicValue dicValue) {
        return dicValueMapper.updateDicVal(dicValue);
    }

    @Override
    public DicValue selectDicValueById(String id) {
        return dicValueMapper.selectDicValById(id);
    }

    @Override
    public int deleteDicValueBatch(String[] id) {
        return dicValueMapper.deleteDicValBatch(id);
    }

    @Override
    public List<DicValue> selectDicValByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValByTypeCode(typeCode);
    }

    //在监听器中使用
    @Autowired
    private DicTypeMapper dicTypeMapper;
    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<>();
        //查询字典类型表
        List<DicType> dtList = dicTypeMapper.selectAllDicType();
        //遍历集合，获取到每一种类型
        for (DicType dt : dtList) {
            //根据每一个字典类型去获取字典类型编码
            String code = dt.getCode();
            //根据每一种类型编码去获取所对应的字典值列表
            List<DicValue> dvList = dicValueMapper.selectDicValByTypeCode(code);
            map.put(code+"List", dvList);
        }
        return map;
    }
}
