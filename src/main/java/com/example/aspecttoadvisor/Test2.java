package com.example.aspecttoadvisor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : KaelvihN
 * @date : 2023/9/2 17:12
 */
public class Test2 {
    public static void main(String[] args) {
        //切面对象实例工厂，用于后续反射调用切面中的方法
        SingletonAspectInstanceFactory factory =
                new SingletonAspectInstanceFactory(new Aspect());
        /**
         * 高级切面转低级切面
         */
        //创建存储低级切面的集合
        List<Advisor> advisors = new ArrayList<>();
        //遍历切面的方法，并解析和判断注解
        for (Method method : Aspect.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                //解析切点
                String expression = method.getAnnotation(Before.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                //通知类，前置通知对应的通知类是AspectJMethodBeforeAdvice
                AspectJMethodBeforeAdvice beforeAdvice =
                        new AspectJMethodBeforeAdvice(method, pointcut, factory);
                //有通知，有切点=>可以创建切面
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, beforeAdvice);
                //将创建的低级切面加入到集合中
                advisors.add(advisor);
            }
            //遍历集合
            advisors.forEach(System.out::println);
        }

    }

    static class Aspect {
        @Before("execution(* foo())")
        public void before1() {
            System.out.println("Before1");
        }

        @Before("execution(* foo())")
        public void before2() {
            System.out.println("Before2");
        }

        public void after() {
            System.out.println("After");
        }

        public void afterReturning() {
            System.out.println("AfterReturning");
        }

        public void afterThrowing() {
            System.out.println("AfterThrowing");
        }

        public Object around(ProceedingJoinPoint point) {
            try {
                System.out.println("Around Before ...");
                return point.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println("Around After...");
            }
        }
    }

    static class Target {
        public void foo() {
            System.out.println("Foo");
        }
    }
}
