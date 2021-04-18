package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 杨廷甲
 * 2021-01-04
 */

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public int saveClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int deleteClue(String id) {
        return clueMapper.deleteClueById(id);
    }

    @Override
    public List<Clue> queryClueByCondition(Map<String, Object> map) {
        return clueMapper.selectClueForPageByCondition(map);
    }

    @Override
    public long queryContOfClueCondition(Map<String, Object> map) {
        return clueMapper.selectContOfClueCondition(map);
    }

    @Override
    public List<Activity> queryActivityByClueId(String clueId) {
        return clueMapper.selectActivityByClueId(clueId);
    }

    @Override
    public List<Activity> queryActivityNoBoundById(Map<String,Object> map) {
        return activityMapper.searchActivityNoBoundById(map);
    }

    //进行线索转换
    @Override
    public void saveConvert(Map<String, Object> map) {
        //解析控制层传过来的数据，将需要的数据拿出来
        String clueId =(String) map.get("clueId");
        User user = (User) map.get("sessionUser");
        String isCreateTran = (String)map.get("isCreateTran");

        //根据clueId查询线索信息
        Clue clue = clueMapper.selectClueById(clueId);
        //首先将线索中的公司的信息转换到客户表
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(clue.getOwner());
        customer.setName(clue.getCompany());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());

        customerMapper.insertCustomer(customer);

        //将线索中的联系人信息转换到联系人表中
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullName(clue.getFullName());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contactsMapper.insertContacts(contacts);

        //根据clueId查询该线索下的备注， 将该线索下的备注转入 客户备注和联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        //判读该备注集合是否为空，不为空则进行转入
        if (clueRemarkList != null && clueRemarkList.size() > 0){
            CustomerRemark cur = null;
            ContactsRemark cor = null;

            List<CustomerRemark> curList = new ArrayList<>();
            List<ContactsRemark> corList = new ArrayList<>();

            for (ClueRemark clueRemark : clueRemarkList) {
                cur = new CustomerRemark();
                cur.setCreateBy(clueRemark.getCreateBy());
                cur.setCreateTime(clueRemark.getCreateTime());
                cur.setCustomerId(customer.getId());
                cur.setEditBy(clueRemark.getEditBy());
                cur.setEditFlag(clueRemark.getEditFlag());
                cur.setEditTime(clueRemark.getEditTime());
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(clueRemark.getNoteContent());
                curList.add(cur);

                cor=new ContactsRemark();
                cor.setContactsId(contacts.getId());
                cor.setCreateBy(clueRemark.getCreateBy());
                cor.setCreateTime(clueRemark.getCreateTime());
                cor.setEditBy(clueRemark.getEditBy());
                cor.setEditFlag(clueRemark.getEditFlag());
                cor.setEditTime(clueRemark.getEditTime());
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(clueRemark.getNoteContent());
                corList.add(cor);
            }
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }

        //将线索和市场活动的关系 转换到 联系人和市场活动的关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

        if (carList != null && carList.size() > 0){
            ContactsActivityRelation coar = null;
            List<ContactsActivityRelation> coarList = new ArrayList<>();
            for (ClueActivityRelation relation : carList) {
                coar=new ContactsActivityRelation();
                coar.setId(UUIDUtils.getUUID());
                coar.setActivityId(relation.getActivityId());
                coar.setContactsId(contacts.getId());

                coarList.add(coar);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
        }


        //如果创建了交易，那么就要在交易表中添加一条记录
        if ("true".equals(isCreateTran)){
            Tran tran = new Tran();
            tran.setActivityId((String)map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formatDateTime(new Date()));
            tran.setCustomerId(customer.getId());
            tran.setExpectedDate((String)map.get("expectedClosingDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney((String)map.get("amountOfMoney"));
            tran.setName((String)map.get("tradeName"));
            tran.setOwner(user.getId());
            tran.setStage((String)map.get("stage"));
            tranMapper.insert(tran);

            //在交易表中添加交易记录后，还需要把该线索下所有的备注转入到交易表中
            //遍历clueRemarkList ,封装tranRemark,进行转入
            if (clueRemarkList != null && clueRemarkList.size()> 0){
                TranRemark tr = null;
                List<TranRemark> trList = new ArrayList<>();
                for (ClueRemark clueRemark : clueRemarkList) {
                    tr = new TranRemark();
                    tr.setCreateBy(clueRemark.getCreateBy());
                    tr.setCreateTime(clueRemark.getCreateTime());
                    tr.setEditby(clueRemark.getEditBy());
                    tr.setEditFlag(clueRemark.getEditFlag());
                    tr.setEditTime(clueRemark.getEditTime());
                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(clueRemark.getNoteContent());
                    tr.setTranId(tran.getId());
                    trList.add(tr);
                }
                tranRemarkMapper.insertTranRemarkByList(trList);
            }
        }

        //在交易历史表中创建一条记录
        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney((String)map.get("amountOfMoney"));
        tranHistoryMapper.insert(tranHistory);



        //删除线索备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        //删除线索和市场活动的关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);

        //删除线索
        clueMapper.deleteClueById(clueId);
    }
}
