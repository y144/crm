package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.mapper.TranRemarkMapper;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 杨廷甲
 * 2021-01-09
 */
@Service
public class TranRemarkServiceImpl implements TranRemarkService {

    @Autowired
    TranRemarkMapper tranRemarkMapper;

    @Override
    public List<TranRemark> queryTranRemarkForDetail(String tranId) {
        return tranRemarkMapper.selectTranRemarkForDetailByTranId(tranId);
    }

    @Override
    public int saveTranRemark(TranRemark tranRemark) {
        return tranRemarkMapper.insert(tranRemark);
    }

    @Override
    public int editTranRemark(TranRemark tranRemark) {
        return tranRemarkMapper.updateByPrimaryKey(tranRemark);
    }

    @Override
    public int deleteTranRemark(String id) {
        return tranRemarkMapper.deleteByPrimaryKey(id);
    }
}
