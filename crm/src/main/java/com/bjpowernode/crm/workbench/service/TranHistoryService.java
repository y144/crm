package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;

/**
 * 杨廷甲
 * 2021-01-09
 */
public interface TranHistoryService {

    List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);
}
