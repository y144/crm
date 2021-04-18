package com.crm.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.web.controller.ClueController;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * 杨廷甲
 * 2021-01-05
 */
public class ActivityServiceTest extends BaseTest {


    @Autowired
    private ClueService clueService;


    @Test
    public void test3(){
        HashMap map=new HashMap();
        map.put("activityName","家具");
        map.put("clueId","01b41229673949f7a8196215332aaa70");
        List<Activity> activityList = clueService.queryActivityNoBoundById(map);
        System.out.println(activityList.size());
    }
}
