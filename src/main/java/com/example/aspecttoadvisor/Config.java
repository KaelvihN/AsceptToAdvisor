package com.example.aspecttoadvisor;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : KaelvihN
 * @date : 2023/8/24 17:59
 */

@Configuration
public class Config {
    @Bean
    public Advisor advisor(MethodInterceptor advice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.example.aspecttoadvisor.*.foo())");
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public MethodInterceptor advice() {
        MethodInterceptor advice = invocation -> {
            System.out.println("Advice Before...");
            Object result = invocation.proceed();
            System.out.println("Advice After...");
            return result;
        };
        return advice;
    }
}
