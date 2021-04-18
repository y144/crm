package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 杨廷甲
 * 2020-12-18
 */

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
