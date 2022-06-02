package com.mybatisSoundCode.spring_mybatis;


import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({HansiousImportBeanDefinitionRegister.class})
public @interface HansiousScan {
    String value() default "";
}
