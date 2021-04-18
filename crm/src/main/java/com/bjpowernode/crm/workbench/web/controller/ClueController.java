package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 杨廷甲
 * 2021-01-04
 */

@Controller
public class ClueController {


    @Autowired
    private ClueService clueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    //跳转到线索的页面
    @RequestMapping("/workbench/clue/index.do")
    public String toIndex(Model model){

        //调用service层查询动态数据
        List<User> userList = userService.selectAllUsers();
        List<DicValue> appellationList = dicValueService.selectDicValByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.selectDicValByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.selectDicValByTypeCode("source");

        //把数据保存在作用域中
        model.addAttribute("userList", userList);
        model.addAttribute("appellationList",appellationList );
        model.addAttribute("clueStateList", clueStateList);
        model.addAttribute("sourceList",sourceList );
        return "workbench/clue/index";
    }

    //保存线索信息
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){

        User user = (User) session.getAttribute(Contants.SESSION_USER);

        //封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));

        //创建返回结果集
        ReturnObject returnObject = new ReturnObject();
        //调用service层方法，保存数据
        try {
            int ret = clueService.saveClue(clue);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败！");
        }
        return returnObject;
    }
    //点击名称进行到 线索明细
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,Model model){
        //调用service层方法，查询数据
        Clue clue=clueService.queryClueForDetailById(id);
        List<ClueRemark> remarkList=clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);
        //把数据保存到request中
        model.addAttribute("clue",clue);
        model.addAttribute("remarkList",remarkList);
        model.addAttribute("activityList",activityList);
        //请求转发
        return "workbench/clue/detail";
    }
    //线索分页查找
    @RequestMapping("/workbench/clue/queryClueForPageByCondition.do")
    public @ResponseBody Object queryActivityForByCondition(int pageNo,int pageSize,String fullName,String company,String phone,String mphone,String state,String source,String owner) {
        //创建map封装参数
        Map<String, Object> map = new HashMap();
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("fullName", fullName);
        map.put("company", company);
        map.put("phone", phone);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("source", source);
        map.put("owner", owner);

        //调用service层查询数据
        //根据条件，分页查询市场活动
        List<Clue> clueList = clueService.queryClueByCondition(map);
        //根据条件查询市场活动总数
        long totalRows = clueService.queryContOfClueCondition(map);
        //根据查询结果生成相应的返回信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList", clueList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    //查找未绑定的市场活动
    @RequestMapping("/workbench/clue/searchActivityNoBoundById.do")
    public @ResponseBody Object searchActivity(String activityName,String clueId){
        HashMap<String,Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        List<Activity> activityList = clueService.queryActivityNoBoundById(map);
        return activityList;
    }

    //关联市场活动,一次选择多个市场活动进行关联到一个线索上
    @RequestMapping("/workbench/clue/saveBundActivity.do")
    public @ResponseBody Object saveBundActivity(String[] activityId ,String clueId){
        //封装参数
        //拿到线索与市场活动关系对象
        ClueActivityRelation relation = null;
        //创建一个list存储 线索市场关系对象
        List<ClueActivityRelation> relationList = new ArrayList<>();
        //遍历拿到的市场活动的id集合，每取出一个市场活动id，就创建一个线索与市场活动关系对象，设置属性将参数进行封装将生产的对象，添加到创建的list集合中
        for (String ai : activityId) {
            relation = new ClueActivityRelation();
            relation.setId(UUIDUtils.getUUID());
            relation.setActivityId(ai);
            relation.setClueId(clueId);
            relationList.add(relation);
        }
        ReturnObject returnObject = new ReturnObject();
        try {//调用service层 保存线索和市场活动的关联关系
            int ret = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //添加成功后 查询的到activityId市场活动的信息
                List<Activity> activityList = activityService.exportActivitySelective(activityId);
                //保存到返回的数据集
                returnObject.setRetData(activityList);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }

        return returnObject;
    }

    //解除关联关系
    @RequestMapping("/workbench/clue/saveUnbundActivity.do")
    public @ResponseBody Object saveUnbundActivity(ClueActivityRelation relation){

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，删除线索和市场活动的关联关系
            int ret=clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }

    //转换 (按 转换按钮  先跳转到转换的页面上 请求转发)
    @RequestMapping("/workbench/clue/convertClue.do")
    public String convertClue(String id,Model model){

        //调用service层查询数据
        //按转换后 在页面上要展现线索的相关数据
        Clue clue = clueService.queryClueForDetailById(id);
        //并且还要在阶段下拉列表中展示 字典值的阶段信息
        List<DicValue> stageList = dicValueService.selectDicValByTypeCode("stage");

        model.addAttribute("clue", clue);
        model.addAttribute("stageList",stageList );
        return "workbench/clue/convert";
    }

    //跳转到转换页面上后 进行转换
    @RequestMapping("/workbench/clue/saveConvertClue.do")
    public @ResponseBody Object saveConvertClue(String clueId,String isCreateTran,String amountOfMoney,String tradeName,String expectedClosingDate,String stage,String activityId,HttpSession session){

        //点击转换拿到参数后，将接收到的参数封装起来 传给service层进行业务的处理
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("isCreateTran",isCreateTran);
        map.put("amountOfMoney",amountOfMoney);
        map.put("tradeName",tradeName);
        map.put("expectedClosingDate",expectedClosingDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("sessionUser",session.getAttribute(Contants.SESSION_USER));

        //调用业务层处理线索转换
        clueService.saveConvert(map);
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        return returnObject;
    }
}
