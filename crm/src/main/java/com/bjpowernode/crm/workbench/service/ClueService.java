package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

/**
 * 杨廷甲
 * 2021-01-04
 */
public interface ClueService {

    //增加线索信息
    int saveClue(Clue clue);

    //按主键id进行线索明细查找
    Clue queryClueForDetailById(String id);

    //按主键查找线索信息
    Clue queryClueById(String id);

    //按主键id进行线索删除
    int deleteClue(String id);

    //根据条件分页查找线索
    List<Clue> queryClueByCondition(Map<String,Object> map);

    //统计线索数量
    long queryContOfClueCondition(Map<String,Object> map);

    //根据clueId查询市场活动
    List<Activity> queryActivityByClueId(String clueId);

    //查找还没有关联的市场活动
    List<Activity> queryActivityNoBoundById(Map<String,Object> map);

    //进行线索转换处理
    void saveConvert(Map<String,Object> map);

}
