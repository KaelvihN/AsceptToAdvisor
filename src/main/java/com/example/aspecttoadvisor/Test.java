package com.example.aspecttoadvisor;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author : KaelvihN
 * @date : 2023/8/31 23:51
 */
public class Test {
    public static void main(String[] args) {
        test0();
    }

    public static void test0() {
        //创建一个干净的容器
        GenericApplicationContext context = new GenericApplicationContext();
        //注册自己写的类
        context.registerBean(MyConfig.class);
        //注册BeanFactory的后置处理器
        context.registerBean(ConfigurationClassPostProcessor.class);
        //容器初始化
        context.refresh();
//        //遍历
//        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
//            System.out.println("beanDefinitionName = " + beanDefinitionName);
//        }
        //销毁容器
        context.close();
    }

    @Configuration
    static class MyConfig {
        /**
         * 解析 @AspectJ 注解，产生代理
         */
        @Bean
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
            return new AnnotationAwareAspectJAutoProxyCreator();
        }

        /**
         * 解析 @Autowired
         */
        @Bean
        public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
            return new AutowiredAnnotationBeanPostProcessor();
        }

        /**
         * 解析 @PostConstruct
         */
        @Bean
        public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
            return new CommonAnnotationBeanPostProcessor();
        }

        @Bean
        public Advisor advisor(MethodInterceptor advice) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice);
        }

        @Bean
        public MethodInterceptor advice() {
            return invocation -> {
                System.out.println("before...");
                return invocation.proceed();
            };
        }

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean1 {
        public void foo() {
        }

        public Bean1() {
            System.out.println("Bean1()");
        }

        @PostConstruct
        public void init() {
            System.out.println("Bean1 init()");
        }

        @Autowired
        public void setBean2(Bean2 bean2){
            System.out.println("Bean1 setBean2(bean2) class is: " + bean2.getClass());
        }
    }

    static class Bean2 {
        public Bean2() {
            System.out.println("Bean2()");
        }

        @Autowired
        public void setBean1(Bean1 bean1) {
            System.out.println("Bean2 setBean1(bean1) class is: " + bean1.getClass());
        }

        @PostConstruct
        public void init() {
            System.out.println("Bean2 init()");
        }
    }

}
