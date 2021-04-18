package com.bjpowernode.crm.settings.web.interceptor;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 杨廷甲
 * 2020-12-18
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override  //在拦截器中判断该用户以前是否登录过
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取session
        HttpSession session = httpServletRequest.getSession();
        //判断该session是否存在
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //如果这个session是存在的，那么判断这个session中是否有内容
        if (user == null){
            //如果session里面为空的会那么就在回到登录页面
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
            return false;
        }
        //否则可以直接进行登录
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
