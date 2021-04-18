package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.mapper.DicTypeMapper;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 杨廷甲
 * 2020-12-24
 */

@Service
public class DicTypeServiceImpl implements DicTypeService {

    @Autowired
    private DicTypeMapper dicTypeMapper;

    @Override
    public List<DicType> selectAllDicType() {
        List<DicType> dicTypes = dicTypeMapper.selectAllDicType();
        return dicTypes;
    }

    @Override
    public DicType selectDicTypeById(String code) {
        return dicTypeMapper.selectDicTypeById(code);
    }

    //在开发保存字典类型完成时出现了错误，出现了SQLException异常
    //原因：是事务配置在service层，但是service的保存方法是不符合事务配置时的规定，方法名写成了insert，这个方法名的开头的在事务配置是read-only，所以不能够进行保存
    //措施：修改service层的方法名的开头变成不是read-only的那种配置
    @Override
    public int saveDicType(DicType dicType) {
        int saveDicType = dicTypeMapper.saveDicType(dicType);
        return saveDicType;
    }

    @Autowired
    private DicValueMapper dicValueMapper;

    @Override//在批量删除的时候要首先删除这些类型下的所有数据字典值
    public int deleteDicTypeBatch(String[] codes) {
        dicValueMapper.deleteDicValBatch(codes);
        return dicTypeMapper.deleteDicTypeBatch(codes);
    }

    @Override
    public int updateDicType(DicType dicType) {
        return dicTypeMapper.updateDicType(dicType);
    }
}
