package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranHistoryService;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 杨廷甲
 * 2020-12-26
 */

@Controller
public class TranController {

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TranService tranService;

    @Autowired
    private TranRemarkService tranRemarkService;

    @Autowired
    private TranHistoryService tranHistoryService;


    @RequestMapping("/workbench/tran/queryTranForPageByCondition.do")
    @ResponseBody
    public Object tranForPageByCondition(int pageNo,int pageSize,String name,String owner,String customerId,String stage,String type,String source,String contactsId){
        //创建map封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo", pageNo);
        map.put("pageSize",pageSize );
        map.put("name",name );
        map.put("owner",owner );
        map.put("customerId",customerId );
        map.put("stage", stage);
        map.put("type", type);
        map.put("source", source);
        map.put("contactsId", contactsId);
        //调用service层进行处理 分页查询
        //根据条件，分页查询市场活动
        List<Tran> tranList = tranService.queryTranForPageByCondition(map);
        //根据条件查询市场活动总数
        long totalRows = tranService.queryContOfTranByCondition(map);
        //根据查询结果生成相应的返回信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("tranList", tranList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("workbench/transaction/index.do")
    public String transIndex(Model model){
        //调用service层方法，查询动态数据
        List<DicValue> stageList = dicValueService.selectDicValByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.selectDicValByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.selectDicValByTypeCode("source");
        //把数据保存在request中
        model.addAttribute("stageList", stageList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        model.addAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/transaction/index";
    }

    //跳转到保存交易的页面
    @RequestMapping("workbench/transaction/createTran.do")
    public String toCreatTranIndex(Model model){
        //调用service层方法，查询动态数据
        List<User> userList = userService.selectAllUsers();
        List<DicValue> stageList = dicValueService.selectDicValByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.selectDicValByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.selectDicValByTypeCode("source");
        //把数据保存在request中
        model.addAttribute("userList", userList);
        model.addAttribute("stageList", stageList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        model.addAttribute("sourceList",sourceList);

        return "workbench/transaction/save";
    }

    //根据stage的名称得到交易成功的可能性的值
    @RequestMapping("/workbench/transaction/getPossibilityByStageValue.do")
    public @ResponseBody Object etPossibilityByStageValue(String stageValue){
        //解析properties配置 获得可能性 将文件读取
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        //拿到所对应的可能性的值
        String possibility = bundle.getString(stageValue);
        return possibility;
    }

    //为客户名称添加自动补全功能
    @RequestMapping("/workbench/transaction/queryCustomerByName.do")
    public @ResponseBody Object queryCustomerByName(String customerName){
        //调用service层方法，查询客户
        List<Customer> customerList=customerService.queryCustomerByName(customerName);

        return customerList;
    }

    //添加交易
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(Tran tran, String customerName, HttpSession session){
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        //封装参数
        tran.setId(UUIDUtils.getUUID());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));

        Map<String,Object> map=new HashMap<>();
        map.put("tran",tran);
        map.put("customerName",customerName);
        map.put("sessionUser",user);

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存数据
            tranService.saveCreateTran(map);

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }

        return returnObject;
    }

    //通过交易id查看交易详情
    @RequestMapping("/workbench/tran/detailTran.do")
    public String toTranDetailById(Model model,String id){
        //调用业务层 获取交易的详细信息
        Tran tran = tranService.queryTranForDeatilById(id);
        //获取交易备注
        List<TranRemark> remarkList = tranRemarkService.queryTranRemarkForDetail(id);
        //获得交易历史
        List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryForDetailByTranId(id);

        //解析properties配置 获得可能性 将文件读取
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        //拿到所对应的可能性的值
        String possibility = bundle.getString(tran.getStage());
        //设置交易的可能性
        tran.setPossibility(possibility);
        //获得交易的阶段信息
        List<DicValue> stageList = dicValueService.selectDicValByTypeCode("stage");
        //封装信息，返回给页面
        model.addAttribute("tran", tran);
        model.addAttribute("remarkList",remarkList );
        model.addAttribute("tranHistoryList",tranHistoryList );
        model.addAttribute("stageList", stageList);
        model.addAttribute("possibility", possibility);

        //获取在失败之前最后一个成功的阶段的orderNo(orderNo是已经完成到哪个阶段的计数)
        TranHistory tranHistory = null;
        for (int i = tranHistoryList.size()-1; i >=0; i--) {
            tranHistory = tranHistoryList.get(i);//第一次拿到最后的成功之前的阶段；
            System.out.println(tranHistory.getOrderNo());
            //stageList.size()==9；stageList.size()-3==7； 表示普通阶段orderNo
            int num= Integer.parseInt(stageList.get(stageList.size()-3).getOrderNo());
            //表示如果当前的最后阶段小于7就是在成交普通阶段之内
            if (Integer.parseInt(tranHistory.getOrderNo())<Integer.parseInt(stageList.get(stageList.size()-3).getOrderNo())){
                //将tranHistory中的orderNo保存在作用域中
                model.addAttribute("theOrderNo", tranHistory.getOrderNo());
                break;
            }
        }
        return "workbench/transaction/detail";
    }

}
