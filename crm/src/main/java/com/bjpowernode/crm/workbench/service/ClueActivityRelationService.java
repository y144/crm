package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;


import java.util.List;

/**
 * 杨廷甲
 * 2021-01-05
 */
public interface ClueActivityRelationService {

    //关联市场活动
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);

    //解除关联关系
    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
