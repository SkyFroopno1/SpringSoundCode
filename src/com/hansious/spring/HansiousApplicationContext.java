package com.hansious.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description: Spring容器
 * @author: Han Xiaojie
 * @date: 2022-05-29 15:37
 **/


public class HansiousApplicationContext {

    private Class configClass;

    /*key:beanName, value:beanDefinition */
    private ConcurrentMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    /*key:beanName, value:bean(Object) */
    private ConcurrentMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private ArrayList<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public HansiousApplicationContext(Class configClass) {
        this.configClass = configClass; //  载入配置类

        /*
         * 整体流程: 扫描 -> BeanDefinition --> beanDefinitionMap
         * */

        //  扫描
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            // 获取配置类上是否有扫描注解
            // 如果有，则获取扫描注解
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);

            /* <解析扫描路径代码块> */
            // 拿到注解的值，即扫描路径: com.Hansious.service，这个实际上是一个报名，不是全路径
            // 需要注意的是，我们扫描的不应该是.java文件，而是编译后的.class文件
            String path = componentScanAnnotation.value();
            String relativePath = path.replace(".", "/");  // 相对路径: com/Hansious/service
            // 拿到当前类的加载器
            ClassLoader classLoader = HansiousApplicationContext.class.getClassLoader();
            // 获取 path 这个相对路径所对应的资源
            // 绝对路径: D:/Java/SpringSoundCode/out/production/SpringSoundCode/ + relativePath
            URL resource = classLoader.getResource(relativePath);
//            System.out.println(resource);   // Log
            File file = new File(resource.getFile());
            // 判断当前路径下的文件，是不是一个文件夹
            if (file.isDirectory()) {
                File[] files = file.listFiles(); // 拿到这个文件夹下的所有文件
                // 过滤掉 files 当中, 不是.class的文件。我们只需要.class文件
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    if (fileName.endsWith(".class")) {
                        /* 如果是一个class 文件，那么就通过类加载器获取这个类对象*/
                        // 处理fileName成为类的全限定名,用于后续类加载器生成类(此处处理不灵活，仅为实现功能)
//                        String fullyClassName = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        String className = fileName.substring(fileName.lastIndexOf('\\') + 1, fileName.indexOf(".class")); // 类名
                        String fullyClassName = path + "." + className; // 全限定名
//                        System.out.println("fullyClassName:" + fullyClassName);  // Log
                        try {
                            // 这里应该是类的全限定名,如:com.hansious.service.UserService.
                            Class<?> clazz = classLoader.loadClass(fullyClassName);
                            /*模拟BeanDefinition的生成*/
                            if (clazz.isAnnotationPresent(Component.class)) {
                                // 是不是由BeanPostProcessor接口派生的
                                // 如果是，额外的把这个类放入到集合中
                                if (BeanPostProcessor.class.isAssignableFrom(clazz)){
                                    BeanPostProcessor instance = (BeanPostProcessor) clazz.newInstance();
                                    beanPostProcessors.add(instance);
                                }

                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();
                                if ("".equals(beanName)) {
                                    // 默认beanName是类名的小写
//                                    beanName = className.substring(0,1).toLowerCase() + className.substring(1);
                                    beanName = Introspector.decapitalize(className); // 首字母小写的方法
                                    System.out.println("beanName:" + beanName);
                                }
                                // is a Bean.但我们并不应该在这里创建出来这个实例，因为没法确定是多例Bean还是单例
                                // 生成一个Beandefinition对象
                                BeanDefinition beanDefintion = new BeanDefinition();
                                beanDefintion.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scope = clazz.getAnnotation(Scope.class);
                                    String value = scope.value();
                                    beanDefintion.setScope(value);
                                } else {
                                    beanDefintion.setScope(SpringContant.SINGLETON); // 默认单例
                                }
                                beanDefinitionMap.put(beanName, beanDefintion);


                            }
                        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

        }

        // 实例化单例Bean,并存入单例池
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (SpringContant.SINGLETON.equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }

    }


    /**
     * 创建Bean
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class type = beanDefinition.getType();
        // 需要保证必须有一个无参方法
        try {
            Object instance = type.getConstructor().newInstance();

            /*========== 依赖注入 ===========*/
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName())); // byName方式
                }
            }

            // Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // 初始化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                // 如果进行了对象代理，那么instance就替换为代理对象
                instance = beanPostProcessor.postProcessBeforeInitialization(beanName, instance);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            // BeanPostProcessor  AOP
            // 初始化后
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                // 如果进行了对象代理，那么instance就替换为代理对象
                instance = beanPostProcessor.postProcessAfterInitialization(beanName,instance);
            }



            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Bean的名字获取这个类
     * 同时需要判断是 单例Bean还是多例Bean
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException();
        }
        String scope = beanDefinition.getScope();

        if (SpringContant.SINGLETON.equals(scope)) { // 单例的情况下
            Object bean = singletonObjects.get(beanName);
            if (bean == null) {
                bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);    // 注意这里！我们Bean是单例的，如果为null，生成后需要放入单例池
            }
            return bean;
        } else {    // 多例的情况下
            return createBean(beanName, beanDefinition);
        }

    }
}
