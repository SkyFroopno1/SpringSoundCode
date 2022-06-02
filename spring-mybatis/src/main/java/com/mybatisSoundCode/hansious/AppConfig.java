package com.mybatisSoundCode.hansious;

import com.mybatisSoundCode.spring_mybatis.HansiousImportBeanDefinitionRegister;
import com.mybatisSoundCode.spring_mybatis.HansiousScan;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: 配置类
 * @author: Han Xiaojie
 * @date: 2022-05-31 17:18
 **/

@ComponentScan("com.mybatisSoundCode")
//@Import({HansiousImportBeanDefinitionRegister.class}) // 迁移到HansiousScan
@HansiousScan("com.mybatisSoundCode.hansious.mapper")
public class AppConfig {


    @Bean
    public SqlSessionFactory sqlSessionFactory() throws IOException {
        InputStream is = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(is);
        return build;
    }

}
