package com.crm.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.crm.BaseTest;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 杨廷甲
 * 2020-12-30
 */
public class ActivityRemarkDao extends BaseTest {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Test
    public void test(){
        List<ActivityRemark> activityRemarkList = activityRemarkMapper.selectActivityRemarkByActivityId("b3aa22aa11404f08af4b565349f4364e");
        System.out.println(activityRemarkList.size());
    }
}
