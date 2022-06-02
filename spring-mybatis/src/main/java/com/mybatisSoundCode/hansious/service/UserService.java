package com.mybatisSoundCode.hansious.service;

import com.mybatisSoundCode.hansious.mapper.OrderMapper;
import com.mybatisSoundCode.hansious.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Han Xiaojie
 * @date: 2022-05-31 17:17
 **/

@Component
public class UserService {

    @Autowired
    private UserMapper userMapper;  // 代理对象: 由MyBatis生成 ， UserMapper 需要是一个Bean

    @Autowired
    private OrderMapper orderMapper;

    public void test() {
        System.out.println(userMapper.getUserName());
        System.out.println(orderMapper.getOrder());

    }

}
