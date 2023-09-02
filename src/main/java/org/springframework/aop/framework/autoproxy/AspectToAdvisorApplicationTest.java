package org.springframework.aop.framework.autoproxy;


import com.example.aspecttoadvisor.Config;
import com.example.aspecttoadvisor.MyAspect;
import com.example.aspecttoadvisor.Target1;
import com.example.aspecttoadvisor.Target2;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;


public class AspectToAdvisorApplicationTest {
    public static void main(String[] args) {
        //干净容器
        GenericApplicationContext context = new GenericApplicationContext();
        //注册自己写的Bean
        context.registerBean(Config.class);
        context.registerBean(MyAspect.class);
        //注册BeanFactoryPostProcessor(解析Config中的@Bean)
        context.registerBean(ConfigurationClassPostProcessor.class);
        //注册解析解析切面注解的解析器
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        //容器初始化
        context.refresh();
        AnnotationAwareAspectJAutoProxyCreator creator =
                context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        /**
         * 1 将低级切面加入到集合中
         * 2 将高级切面转化为低级切面斌加入到集合中
         */
//        List<Advisor> advisors = creator.findEligibleAdvisors(Target1.class, "target1");
        List<Advisor> advisors = creator.findEligibleAdvisors(Target2.class, "target2");
        advisors.forEach(ad -> System.out.println("ad=" + ad));
        /**
         * 调用findEligibleAdvisors，返回的集合不为空，就创建代理
         */
        Object o1 = creator.wrapIfNecessary(new Target1(), "target1", "target1");
        Object o2 = creator.wrapIfNecessary(new Target2(), "target2", "target2");
        System.out.println("o1class = " + o1.getClass());
        System.out.println("o2class = " + o2.getClass());
        ((Target1) o1).foo();
        //销毁容器
        context.close();
    }

}
