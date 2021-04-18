package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import javax.lang.model.element.NestingKind;
import java.util.List;
import java.util.Map;


/**
 * 杨廷甲
 * 2020-12-28
 */

public interface ActivityService {
    //添加保存市场活动
    int saveCreateActivity(Activity activity);

    //按分页查询
    List<Activity> queryActivityForPageByCondition(Map<String,Object> map);

    //查询总条数
    long queryContOfActivityByCondition(Map<String,Object> map);

    //根据id查询
    Activity queryActivityById(String id);

    //保存编辑
    int saveEditActivityById(Activity activity);

    //批量删除
    int deleteActivityByIds(String[] ids);

    //批量导出， 查询全部的市场活动
    List<Activity> queryAllActivityForDetail();

    //根据id查询市场活动的明细
    Activity queryActivityForDetailById(String id);

    //批量导入市场活动
    int saveActivityByList(List<Activity> activityList);

    //选择批量导出市场活动
    List<Activity> exportActivitySelective(String[] ids);

    //根据线索id查找 市场活动
    List<Activity> queryActivityForDetailByClueId(String clueId);


}
