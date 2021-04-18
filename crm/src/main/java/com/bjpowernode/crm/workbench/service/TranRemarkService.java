package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TranRemark;

import java.util.List;

/**
 * 杨廷甲
 * 2021-01-09
 */
public interface TranRemarkService {

    List<TranRemark> queryTranRemarkForDetail(String tranId);

    int saveTranRemark(TranRemark tranRemark);

    int editTranRemark(TranRemark tranRemark);

    int deleteTranRemark(String id);
}
