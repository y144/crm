package com.crm.dao;

import com.alibaba.druid.support.ibatis.SpringIbatisBeanTypeAutoProxyCreator;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.PortUnreachableException;
import java.security.PublicKey;
import java.time.Period;
import java.util.HashMap;
import java.util.List;

/**
 * 杨廷甲
 * 2020-12-28
 */
public class ActivityDao extends BaseTest {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ClueService clueService;

    @Test
    public void testActivityDao1(){
        Activity activity = new Activity();
        activity.setId(UUIDUtils.getUUID());
        int insertActivity = activityMapper.insertActivity(activity);
        if (insertActivity > 0){
            System.out.println("添加成功！");
        }
    }

    @Test
    public void testSelectByCondition(){
        HashMap map = new HashMap();
        map.put("name", "家具");
        map.put("beginNo", 0);
        map.put("pageSize",2);
        List list = activityMapper.selectActivityForPageByCondition(map);
        System.out.println(list.size());
    }

    @Test
    public void testSelectAllByDetail(){
        List<Activity> activityList = activityMapper.queryAllActivityForDetail();
        System.out.println(activityList.size());
    }

    @Test
    public void test3(){
        HashMap map=new HashMap();
        map.put("activityName","家具");
        map.put("clueId","01b41229673949f7a8196215332aaa70");
        List<Activity> activityList = clueService.queryActivityNoBoundById(map);
        System.out.println(activityList.size());
    }
}
