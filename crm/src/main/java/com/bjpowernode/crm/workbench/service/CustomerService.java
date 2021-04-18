package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

/**
 * 杨廷甲
 * 2021-01-07
 */
public interface CustomerService {
    List<Customer> queryCustomerByName(String name);
}
