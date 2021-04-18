package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 杨廷甲
 * 2020-12-21
 */
//获取页面信息
@Controller
public class UserController {


    @RequestMapping("/settings/qx/user/toLogin.do")  //需要获取的信息
    public String toLogin(HttpServletRequest request){
        //十天免登陆
        Cookie[] cookies = request.getCookies();
        String loginAct = null;
        String loginPwd = null;
        if (cookies != null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("loginAct".equals(name)){
                    loginAct = cookie.getValue();
                    continue;
                }
                if ("loginPwd".equals(name)){
                    loginPwd = cookie.getValue();

                }
            }
        }
        if (loginAct != null && loginPwd != null){
            //将cookie中取得的参数进行封装 然后进行验证
            Map<String,Object> map = new HashMap<>();
            map.put("loginAct", loginAct);
            map.put("loginPwd",MD5Util.getMD5(loginPwd));
            //调用service层，将封装好的对象数据进行验证
            User user = userService.selectByNameAndPwd(map);
            //将用户信心保存在session在中
            request.getSession().setAttribute("sessionUser", user);
            return "redirect:/workbench/index.do";
        }else {
            //返回登录页面
            return "settings/qx/user/login";
        }
    }


    @Autowired
    private UserService userService;

    @RequestMapping("settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct,String loginPwd,String isRemPwd,HttpServletRequest request,HttpServletResponse response,HttpSession session){

        //在封装之前使用MD5
        String newLoginPwd = MD5Util.getMD5(loginPwd);
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",newLoginPwd );

        //调用业务层
        User user = userService.selectByNameAndPwd(map);

        System.out.println(user);
        //创建一个返回集对象
        ReturnObject returnObject = new ReturnObject();
        //根据查询结果，生成返回对象中的相关信息，code message
        if (user == null){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或者密码不正确");
        }else {
            if (DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime())>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号时间已经过期");
            }else if ("0".equals(user.getLockState())){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已经被锁定");
                //判断允许的IP地址getAllowIps()拿到一个数组192.168.1.1,127.0.0.1得到这个地址
                //request.getRemoteAddr()这个方法可以拿到当前这个登录的这个IP地址
                //将这个取出来的地址加入到数据库中  0:0:0:0:0:0:0:1
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("IP受限");
            }else {
                //否则登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //将登录成功的user保存到session中以便于后期登录判断
                session.setAttribute(Contants.SESSION_USER, user);


                //在这里对是否进行选择的十天免登陆的功能进行处理
                if ("true".equals(isRemPwd)){
                    //如果用户选择则了这个十天免登陆的这个功能，则将其信息存入cookie中，返回给用户浏览器
                    Cookie c1 = new Cookie("loginAct", loginAct);
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);

                    Cookie c2 = new Cookie("loginPwd", loginPwd);
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2)  ;
                }else {
                    //如果用户没有选择十天免登陆的这个功能呢则用户的信心 不保存cookie
                    Cookie c1 = new Cookie("loginAct", null);
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", null);
                    c1.setMaxAge(0);
                    response.addCookie(c2);

                }
            }
        }
        return returnObject;  //将这个带有信息的对象返回给前端，在这里是通过@ResponseBody这个注解就可以将这个对象直接转换为json格式的字符串，然后在传送至前端
    }



    //退出的功能
    @RequestMapping("/settings/qx/user/logout.do")
    public String loginOut(HttpServletResponse response,HttpSession session){
        //退出系统，清除cookie
        Cookie c1 = new Cookie("loginAct", null);
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", null);
        c1.setMaxAge(0);
        response.addCookie(c2);
        //销毁session
        session.invalidate();
        return "redirect:/";
    }



}
