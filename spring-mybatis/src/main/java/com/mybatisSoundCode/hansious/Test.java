package com.mybatisSoundCode.hansious;

import com.mybatisSoundCode.hansious.mapper.OrderMapper;
import com.mybatisSoundCode.hansious.mapper.UserMapper;
import com.mybatisSoundCode.hansious.service.UserService;
import com.mybatisSoundCode.spring_mybatis.HansiousFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description: 测试
 * @author: Han Xiaojie
 * @date: 2022-05-31 19:41
 **/


public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 迁移到importBeanDefinitionRegistrar
        /*AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
        beanDefinition.setBeanClass(HansiousFactoryBean.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(UserMapper.class);
        context.registerBeanDefinition("userMapper",beanDefinition);
        context.refresh();


        AbstractBeanDefinition beanDefinition2 = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
        beanDefinition.setBeanClass(HansiousFactoryBean.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(OrderMapper.class);
        context.registerBeanDefinition("orderMapper",beanDefinition2);
        context.refresh();*/

//        System.out.println(context.getBean("hansiousFactoryBean"));
//        System.out.println(context.getBean("&hansiousFactoryBean"));
       ((UserService) context.getBean("userService")).test();

    }
}
