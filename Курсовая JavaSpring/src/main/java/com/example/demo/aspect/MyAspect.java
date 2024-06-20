package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class MyAspect {

    @Pointcut("execution(* com.example.demo.*.*(..))")
    public void vaadinMethods() {}
    @Around("vaadinMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Entering method: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        System.out.println("Exiting method: " + joinPoint.getSignature().getName());
        return result;
    }
}