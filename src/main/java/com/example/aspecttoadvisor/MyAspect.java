package com.example.aspecttoadvisor;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

/**
 * @author : KaelvihN
 * @date : 2023/8/24 17:43
 */

@Aspect
@Order(1)
public class MyAspect {
    @Before("execution(* com.example.aspecttoadvisor.*.foo())")
    public void before(){
        System.out.println("MyAspect Before...");
    }

    @After("execution(* com.example.aspecttoadvisor.*.foo())")
    public void after(){
        System.out.println("MyAspect After...");
    }
}
