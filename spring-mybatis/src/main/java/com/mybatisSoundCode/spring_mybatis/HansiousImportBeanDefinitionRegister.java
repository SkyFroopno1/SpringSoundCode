package com.mybatisSoundCode.spring_mybatis;

import com.mybatisSoundCode.hansious.mapper.OrderMapper;
import com.mybatisSoundCode.hansious.mapper.UserMapper;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Map;

/**
 * @description:
 * @author: Han Xiaojie
 * @date: 2022-05-31 20:52
 **/


public class HansiousImportBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(HansiousScan.class.getName());
        String path = (String) annotationAttributes.get("value");

        HansiousScanner hansiousScanner = new HansiousScanner(registry);
        hansiousScanner.addIncludeFilter(new TypeFilter() {
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return true;
            }
        });
        hansiousScanner.scan(path);

        /*AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
        beanDefinition.setBeanClass(HansiousFactoryBean.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(UserMapper.class);
        registry.registerBeanDefinition("userMapper",beanDefinition);


        AbstractBeanDefinition beanDefinition2 = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
        beanDefinition.setBeanClass(HansiousFactoryBean.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(OrderMapper.class);
        registry.registerBeanDefinition("orderMapper",beanDefinition2);*/
    }
}
