package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 杨廷甲
 * 2021-01-09
 */
@Controller
public class CustomerController {


    @RequestMapping("/workbench/customer/index.do")
    public String toCustomerIndex(){
        return "workbench/customer/index";
    }
}
