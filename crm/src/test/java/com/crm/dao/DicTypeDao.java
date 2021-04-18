package com.crm.dao;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.mapper.DicTypeMapper;
import com.bjpowernode.crm.settings.mapper.TblUserMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 杨廷甲
 * 2020-12-24
 */
public class DicTypeDao extends BaseTest {


    @Autowired
    private DicTypeMapper dicTypeMapper;

    @Test
    public void testDicType(){
        List<DicType> dicTypes = dicTypeMapper.selectAllDicType();
        System.out.println(dicTypes.size());
    }

    @Test
    public void testSaveDicType(){
        DicType dicType = new DicType();
        dicType.setCode("ds");
        dicType.setName("fdjklgad过来的看法桑");
        dicType.setDescription("攻击力大开发工具两款发动机");
        int saveDicType = dicTypeMapper.saveDicType(dicType);
        if (saveDicType > 0){
            System.out.println("添加成功");
        }else {
            System.out.println("添加失败");
        }
    }
}
