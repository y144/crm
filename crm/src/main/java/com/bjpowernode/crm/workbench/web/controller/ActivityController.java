package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

/**
 * 杨廷甲
 * 2020-12-28
 */

@Controller
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    //跳转到市场活动的主页面
    @RequestMapping("/workbench/activity/index.do")
    public String activityIndex(Model model){
        //将创建者用户信息带回到页面
        List<User> userList = userService.selectAllUsers();
        model.addAttribute("userList", userList);
        return "workbench/activity/index";
    }

    //保存创建市场活动
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session){
        //市场活动中的创建者就是用户，而用户的信息在session中拿来直接用就行了
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //市场活动信息 的id是不需要的填写的在后台 使用UUID自动生成
        activity.setId(UUIDUtils.getUUID());
        //市场活动的创建时间 在后台根据系统当时时间创建
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());
        //创建返回结果集
        ReturnObject returnObject = new ReturnObject();
        int ret = activityService.saveCreateActivity(activity);
        if (ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }
        return returnObject;
    }

    //实现分页查询，数据展示
    @RequestMapping("/workbench/activity/queryActivityForPageByCondition.do")
    public @ResponseBody Object queryActivityForByCondition(int pageNo,int pageSize,String name,String owner,String startDate,String endDate){
        //创建map封装参数
        Map<String ,Object> map = new HashMap();
        map.put("pageNo", (pageNo-1)*pageSize);
        map.put("pageSize", pageSize);
        map.put("name",name );
        map.put("owner",owner );
        map.put("startDate",startDate );
        map.put("endDate", endDate);
        //调用service层查询数据
             //根据条件，分页查询市场活动
        List<Activity> activityList = activityService.queryActivityForPageByCondition(map);
            //根据条件查询市场活动总数
        long totalRows = activityService.queryContOfActivityByCondition(map);
        //根据查询结果生成相应的返回信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    //当点击修改按钮时，会弹出一个模态窗口，这时就需要按主键查询单个市场活动信息，将要修改的信息展示在模态窗口上
    @RequestMapping("/workbench/activity/editActivity.do")
    public @ResponseBody Object editActivity(String id){
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    //当修改休息展现在模态窗口上时，可进行更改生成一个新的活动对象，再将这个新的市场活动进行保存
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity,HttpSession session){
        //再添加活动时，用户的信息，在后台中的session中自动填充
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //添加活动的更新者 登陆者就是修改者
        activity.setEditBy(user.getId());
        //添加活动的更新时间
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        //创建返回结果集
        ReturnObject returnObject = new ReturnObject();
        //进行添加
        try {
            int ret = activityService.saveEditActivityById(activity);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更新失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败！");
        }
        return returnObject;
    }

    //批量删除
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public @ResponseBody Object deleteActivityByIds(String[] id){
        //创建返回结果集
        ReturnObject returnObject = new ReturnObject();
        //进行删除
        try {
            int ret = activityService.deleteActivityByIds(id);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败！！");
        }
        return returnObject;
    }

    //批量导出
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletRequest request, HttpServletResponse response)throws Exception{
        //将数据库中的市场活动信息取出来
        List<Activity> activityList = activityService.queryAllActivityForDetail();
        //创建HSSFWorkbook,就是创建一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //给这个Excel文件创建一页Excel表格
        HSSFSheet sheet = wb.createSheet("市场活动表");
        //给这一页Excel表格创建 第一行
        HSSFRow row = sheet.createRow(0);

        //创建表头，创建列，为第一行的表头设置标题信息
        HSSFCell cell = row.createCell(0);//第一列
        cell.setCellValue("ID");//第一列是ID
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建者");
        cell=row.createCell(8);
        cell.setCellValue("创建时间");
        cell=row.createCell(9);
        cell.setCellValue("修改者");
        cell=row.createCell(10);
        cell.setCellValue("修改时间");

        //创建样式
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//设置样式为居中

        //填充表单数据
        if (activityList != null){
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                //取出每一条数据
                activity = activityList.get(i);
                //每一条数据就是一个市场活动信息，就是一行Excel表中的信息
                row = sheet.createRow(i + 1);
                //将取出来的一条数据，添加达到Excel表中的创建的这一行的每一列
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());

                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());

                cell=row.createCell(2);
                cell.setCellValue(activity.getName());

                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditBy());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditTime());
            }
        }

        //设置相应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //不同的浏览器接收响应头采用的编码格式不同
        //在请求头中有一个User-Agent属性,来得到当前的使用的浏览器
        String browser = request.getHeader("User-Agent");
        // 火狐采用的编码格式是ISO8859-1
        //对所下载的文件名进行中文转换
        String fileName = URLEncoder.encode("市场活动列表", "UTF-8"); //utf-8进行转换浏览器的编码合适

        //如果得到的浏览器的信息中包含Firefox则就是火狐浏览器，对火狐浏览器进行文件名进行中文转换，编码格式，设置。也可以对其他浏览器进行验证，设置相应的文件名中文转换
        if (browser.contains("chrome")){
            fileName = new String("市场活动列表".getBytes("UTF-8"),"ISO8859-1");
        }
        //在相应对象中添加头信息，告诉浏览器要传输对应的内容 attachment附件+filename文件名
        response.addHeader("Content-Disposition", "attachment;filename="+fileName+".xls");


        //获取输出流将信息输出
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        wb.close();
    }


    //文件上传
    /*
    * 1.导包commons-fileupload
    * 2.配置springmvc上传的内容
    * 3.在fileuploadtest.jsp页面中写上传表单，写三点 1.file 2.post 3.multipart=form-data
    * 4.在控制层进行处理，先创建File，取出要上传文件名，MultipartFile.transferTo()
    * 5.workbench/activity/fileDownLoad.do 请求和fileDownLoad请求是在fileuploadtest.jsp文件中发送出的
    * */
    //上传文件的核心代码
    @RequestMapping("workbench/activity/fileUpload.do")//上传的文件由MultipartFile这个类所对应的参数接收myFile，还可以接收到用户名
    public @ResponseBody Object fileUpload(MultipartFile myFile,String userName)throws Exception{

        //返回结果集
        ReturnObject returnObject = new ReturnObject();
        //假设将文件上传到d:/testDir  先获取文件名
        String originalFilename = myFile.getOriginalFilename();
        //创建一个文件对象 类接收这个上传的文件,第一个参数是文件保存的路径，第二个参数是上传文件名（现在是空的）
        File file = new File("d:\\testDir",originalFilename);
        //将上传文件写入 到这个新建的文件
        myFile.transferTo(file);
        //设置返回结果成功的标志
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        return returnObject;
    }

    //文件下载的核心代码
    @RequestMapping("workbench/activity/fileDownLoad.do")
    public void fileDownLoad(HttpServletRequest request,HttpServletResponse response)throws Exception{
        //读取服务器磁盘上的一个文件(student.xls)，返回浏览器
        //1.设置相应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //根据HTTP协议的规定，浏览器每次向服务器发送请求，都会把浏览器的信息以请求头的形式发送到服务器
        //不同的浏览器接收响应头采用的编码格式不同
        //在请求头中有一个User-Agent属性,来得到当前的使用的浏览器
        String browser = request.getHeader("User-Agent");
        //对所下载的文件名进行中文转换
        String fileName = URLEncoder.encode("市场活动列表", "UTF-8"); //utf-8进行转换浏览器的编码合适

        if (browser.contains("chrome")){
            fileName = new String("市场活动列表".getBytes("UTF-8"),"ISO8859-1");
        }
        //在相应对象中添加头信息，告诉浏览器要传输对应的内容 attachment附件+filename文件名
        //默认情况，浏览器接收到响应信息之后，直接在显示窗口中打开
        //可以设置响应头信息，使浏览器接收到响应信息之后在下载窗口打开
        response.addHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
        //获取输出流
        OutputStream os = response.getOutputStream();
        //读取student.xls文件，通过os输出到浏览器
        InputStream is = new FileInputStream("d:\\testDir\\student.xls");
        //指定一个字节数字，每次向硬盘上传输指定的大小
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len=is.read(buff))!= -1){
            os.write(buff,0,len);
        }
        //关闭资源
        is.close();
        os.flush();
    }

    //文件上传
    @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile,String username,HttpSession session)throws Exception{

        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //创建一个map，将返回所有结果封装到map集合中
        Map<String,Object> retMap = new HashMap<>();
        //创建一个list ，解析Excel文件，获取每一个activity，将所有activity封装在list集合中
        List<Activity> activityList = new ArrayList<>();

        //获取上传的Excel文件
        InputStream is = activityFile.getInputStream();
        //创建一个工作book
         HSSFWorkbook wb = new HSSFWorkbook(is);
         //去第一页
        HSSFSheet sheet = wb.getSheetAt(0);
        // 设置行、列、活动对象
        HSSFRow row = null;
        HSSFCell cell = null;
        Activity activity = null;

        for (int i = 1; i <sheet.getLastRowNum() ; i++) {
            row = sheet.getRow(i);
            activity = new Activity();
            //activity数据来自两部分，一部分时动态的，不在Excel中，程序员给，另一部分从Excel对应的行中获取
            activity.setId(UUIDUtils.getUUID());
            activity.setOwner(user.getId());
            activity.setCreateBy(user.getId());
            activity.setCreateTime(DateUtils.formatDateTime(new Date()));

            //将用户上传的Excel模板文件进行解析，将每一个cell中的数据放入activity
            for (int j = 0; j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                String cellValue = getCellValue(cell);
                if(j==0){
                    activity.setName(cellValue);
                }else if(j==1){
                    activity.setStartDate(cellValue);
                }else if(j==2){
                    activity.setEndDate(cellValue);
                }else if(j==3){
                    activity.setCost(cellValue);
                }else if(j==4){
                    activity.setDescription(cellValue);
                }
            }
            activityList.add(activity);
        }

        //调用业务层
        int ret = activityService.saveActivityByList(activityList);
        //保存前端需要的结果
        retMap.put("code", Contants.RETURN_OBJECT_CODE_SUCCESS);
        retMap.put("count", ret);
        return retMap;
    }

    //判断单元格中的类型，得到单元格中的值
    public static String getCellValue(HSSFCell cell) {
        String ret = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                ret = cell.getCellFormula();
                break;
            default:
                ret = "";
        }
        return ret;
    }


    //选择导出
    @RequestMapping("/workbench/activity/exportActivitySelective.do")
    public void expertActivitySelective(String[] id,HttpServletRequest request,HttpServletResponse response)throws Exception{
        //根据条件查询
        List<Activity> activityList = activityService.exportActivitySelective(id);
        //根据查询条件生成Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //给这个Excel文件创建一页Excel表格
        HSSFSheet sheet = wb.createSheet("市场活动表");
        //给这一页Excel表格创建 第一行
        HSSFRow row = sheet.createRow(0);

        //创建表头，创建列，为第一行的表头设置标题信息
        HSSFCell cell = row.createCell(0);//第一列
        cell.setCellValue("ID");//第一列是ID
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建者");
        cell=row.createCell(8);
        cell.setCellValue("创建时间");
        cell=row.createCell(9);
        cell.setCellValue("修改者");
        cell=row.createCell(10);
        cell.setCellValue("修改时间");

        //创建样式
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//设置样式为居中

        //填充表单数据
        if (activityList != null){
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                //取出每一条数据
                activity = activityList.get(i);
                //每一条数据就是一个市场活动信息，就是一行Excel表中的信息
                row = sheet.createRow(i + 1);
                //将取出来的一条数据，添加达到Excel表中的创建的这一行的每一列
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());

                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());

                cell=row.createCell(2);
                cell.setCellValue(activity.getName());

                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditBy());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditTime());
            }
        }

        //设置相应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //不同的浏览器接收响应头采用的编码格式不同
        //在请求头中有一个User-Agent属性,来得到当前的使用的浏览器
        String browser = request.getHeader("User-Agent");
        // 火狐采用的编码格式是ISO8859-1
        //对所下载的文件名进行中文转换
        String fileName = URLEncoder.encode("市场活动列表", "UTF-8"); //utf-8进行转换浏览器的编码合适

        //如果得到的浏览器的信息中包含Firefox则就是火狐浏览器，对火狐浏览器进行文件名进行中文转换，编码格式，设置。也可以对其他浏览器进行验证，设置相应的文件名中文转换
        if (browser.contains("chrome")){
            fileName = new String("市场活动列表".getBytes("UTF-8"),"ISO8859-1");
        }
        //在相应对象中添加头信息，告诉浏览器要传输对应的内容 attachment附件+filename文件名
        response.addHeader("Content-Disposition", "attachment;filename="+fileName+".xls");


        //获取输出流将信息输出
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        wb.close();
    }
}
