package com.hansious.service;

import com.hansious.spring.*;
import javafx.fxml.Initializable;

/**
 * @description: 模拟Spring测试
 * @author: Han Xiaojie
 * @date: 2022-05-29 15:36
 **/

@Component
@Scope("singleton")
public class UserService implements BeanNameAware, InitializingBean,UserInterface {
    @Autowired
    private OrderService orderService;

    private String beanName;

    private String whatever;

    @Override
    public void test() {
        System.out.println(orderService);
    }



    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        //
        System.out.println("initial");
    }

    public void beforeTest() {
        System.out.println("before...");
    }

    public void afterTest() {
        System.out.println("after...");
    }
}
