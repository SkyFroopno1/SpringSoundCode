package com.hansious.service;

import com.hansious.spring.Autowired;
import com.hansious.spring.Component;

/**
 * @description: 用于测试依赖注入
 * @author: Han Xiaojie
 * @date: 2022-05-29 19:39
 **/

@Component
public class OrderService {

    @Autowired
    private UserService userService;

}
