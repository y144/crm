package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 杨廷甲
 * 2021-01-04
 */
public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        //获取application
        ServletContext application = servletContextEvent.getServletContext();
        DicValueService dicValueService = WebApplicationContextUtils.getWebApplicationContext(application).getBean(DicValueService.class);
        Map<String, List<DicValue>> all = dicValueService.getAll();
        Set<String> set = all.keySet();
        for (String key : set) {
            application.setAttribute(key, all.get(key));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
