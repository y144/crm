package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 杨廷甲
 * 2020-12-21
 */

@Controller
public class WorkBenchController {

    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }

    @RequestMapping("/workbench/main/index.do")
    public String mainIndex(){
        return "workbench/main/index";
    }
}
