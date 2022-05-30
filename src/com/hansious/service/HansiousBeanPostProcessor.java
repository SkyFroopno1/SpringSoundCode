package com.hansious.service;

import com.hansious.spring.BeanPostProcessor;
import com.hansious.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: 测试后置处理器
 * @author: Han Xiaojie
 * @date: 2022-05-29 20:05
 **/


@Component
public class HansiousBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")){
            UserService userService = (UserService) bean;
            userService.beforeTest();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")){
            UserService userService = (UserService) bean;
            userService.afterTest();

            // 基于JDK动态代理生成代理对象
            Object proxyInstance = Proxy.newProxyInstance(HansiousBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("切面逻辑");
                            return method.invoke(bean,args);
                        }
                    });
            return proxyInstance;
        }

        return bean;
    }
}
