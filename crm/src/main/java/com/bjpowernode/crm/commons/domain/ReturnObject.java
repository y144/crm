package com.bjpowernode.crm.commons.domain;

/**
 * 杨廷甲
 * 2020-12-21
 */

  //在这里创建一个统一的返回的结果集
public class ReturnObject {

    //返回的状态
    private String code;

    //返回的信息
    private String message;
    //返回的数据
    private Object retData;


    public String getCode() {
        return code;
    }

    public ReturnObject setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ReturnObject setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getRetData() {
        return retData;
    }

    public ReturnObject setRetData(Object retData) {
        this.retData = retData;
        return this;
    }
}
