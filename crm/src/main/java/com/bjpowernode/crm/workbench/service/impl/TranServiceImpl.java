package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 杨廷甲
 * 2021-01-07
 */
@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;
    @Override//创建交易 就需要创建客户 以及创建交易历史
    public int saveCreateTran(Map<String, Object> map) {
        //将控制层拿到的数据进行处理
        Tran tran =(Tran) map.get("tran");
        String customerId = tran.getCustomerId();
        String customerName =(String) map.get("customerName");
        User user =(User) map.get("sessionUser");

        //判断是否需要创建客户
        if (customerId==null||customerId.trim().length()==0){
            Customer customer = new Customer();
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customer.setCreateBy(user.getId());

            int ret = customerMapper.insertCustomer(customer);

            //把customer的id设置到tran对象中
            tran.setCustomerId(customer.getId());
        }
        //保存交易
        tranMapper.insertTran(tran);

        //保存交易历史记录
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHistory(tranHistory);
        return 0;
    }

    @Override
    public Tran queryTranForDeatilById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }

    @Override
    public List<Tran> queryTranForPageByCondition(Map<String, Object> map) {
        return tranMapper.selectTranForPageByCondition(map);
    }

    @Override
    public long queryContOfTranByCondition(Map<String, Object> map) {
        return tranMapper.selectContOfTranByCondition(map);
    }
}
