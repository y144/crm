package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

/**
 * 杨廷甲
 * 2021-01-07
 */
public interface TranService {

    //创建交易
    int saveCreateTran(Map<String,Object> map);

    //根据id查找交易信息
    Tran queryTranForDeatilById(String id);

    //
    List<FunnelVO> queryCountOfTranGroupByStage();

    //按分页查询
    List<Tran> queryTranForPageByCondition(Map<String,Object> map);

    //查询总条数
    long queryContOfTranByCondition(Map<String,Object> map);
}
