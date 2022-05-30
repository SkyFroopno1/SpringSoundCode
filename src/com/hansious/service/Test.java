package com.hansious.service;

import com.hansious.spring.HansiousApplicationContext;

/**
 * @description: 测试
 * @author: Han Xiaojie
 * @date: 2022-05-29 15:36
 **/


public class Test {
    public static void main(String[] args) {

        // 实例化容器 / 启动容器
        HansiousApplicationContext hansiousApplicationContext =
                new HansiousApplicationContext(AppConfig.class);

        // 测试获取Bean
//        UserService userService = (UserService) hansiousApplicationContext.getBean("userService");
        // 测试单例/多例Bean
//        System.out.println(hansiousApplicationContext.getBean("userService"));
//        System.out.println(hansiousApplicationContext.getBean("userService"));
//        System.out.println(hansiousApplicationContext.getBean("userService"));
//        System.out.println(hansiousApplicationContext.getBean("userService"));

//        userService.test();

        // 测试AOP
        UserInterface userService = (UserInterface) hansiousApplicationContext.getBean("userService");
        userService.test();

    }
}
