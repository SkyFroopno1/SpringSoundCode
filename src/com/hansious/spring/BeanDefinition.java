package com.hansious.spring;

/**
 * @description: Bean的定义信息
 * @author: Han Xiaojie
 * @date: 2022-05-29 17:07
 **/


public class BeanDefinition {

    private Class type;
    private String scope;


    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
