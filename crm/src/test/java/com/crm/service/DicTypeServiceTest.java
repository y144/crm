package com.crm.service;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.service.DicTypeService;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 杨廷甲
 * 2020-12-24
 */
public class DicTypeServiceTest extends BaseTest {

    @Autowired
    private DicTypeService dicTypeService;

    @Test
    public void testDicTypeService(){
        List<DicType> dicTypes = dicTypeService.selectAllDicType();
        System.out.println(dicTypes.size());
    }
}
