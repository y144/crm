package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 杨廷甲
 * 2021-01-04
 */
@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarkForDetailByClueId(String id) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(id);
    }

    @Override
    public int insertClueRemark(ClueRemark remark) {
        return clueRemarkMapper.insert(remark);
    }

    @Override
    public int editClueRemark(ClueRemark remark) {
        return clueRemarkMapper.updateByPrimaryKey(remark);
    }

    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteByPrimaryKey(id);
    }
}
