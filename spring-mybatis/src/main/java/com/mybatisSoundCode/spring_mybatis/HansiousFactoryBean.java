package com.mybatisSoundCode.spring_mybatis;

import com.mybatisSoundCode.hansious.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: Han Xiaojie
 * @date: 2022-05-31 19:48
 **/

//@Component    // 配置DefinitionBean后，就不需要Component了
public class HansiousFactoryBean implements FactoryBean {

    private SqlSession sqlSession;

    private Class aClass;

    public HansiousFactoryBean(Class aClass) {
        this.aClass = aClass;
    }

    @Autowired
    public void setSqlSession(SqlSessionFactory sqlSessionFactory) {
//        sqlSessionFactory.getConfiguration().addMapper(UserMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(aClass);
        this.sqlSession = sqlSessionFactory.openSession();
    }

    public Object getObject() throws Exception {
       /* Object proxyInstance = Proxy.newProxyInstance(HansiousFactoryBean.class.getClassLoader(), new Class[]{UserMapper.class}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName());
                return null;
            }
        });
         return proxyInstance;*/
//        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        return mapper;
        return sqlSession.getMapper(aClass);
    }

    public Class<?> getObjectType() {
//        return UserMapper.class;
        return aClass;
    }
}
