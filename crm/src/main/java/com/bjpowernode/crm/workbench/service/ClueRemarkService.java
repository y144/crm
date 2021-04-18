package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import javax.print.DocFlavor;
import java.util.List;

/**
 * 杨廷甲
 * 2021-01-04
 */
public interface ClueRemarkService {

    //查询线索备注细节根据线索id
    List<ClueRemark> queryClueRemarkForDetailByClueId(String id);

    //添加线索备注信息
    int insertClueRemark(ClueRemark remark);

    //修改线索备注信息
    int editClueRemark(ClueRemark remark);

    //删除线索备注信息
    int deleteClueRemarkById(String id);
}
