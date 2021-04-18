package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * 杨廷甲
 * 2020-12-30
 */
public interface ActivityRemarkService {

    //根据市场活动id查询市场活动备注信息
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

    //增加备注信息
    int saveActivityRemark(ActivityRemark activityRemark);

    //删除备注信息
    int deleteActivityRemarkById(String id);

    //更新备注信息
    int updateActivityRemark(ActivityRemark activityRemark);
}
