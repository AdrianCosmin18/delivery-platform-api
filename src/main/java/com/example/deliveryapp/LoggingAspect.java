package com.example.deliveryapp;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.deliveryapp.user.UserServiceImpl.*(..))")
    public void beforeLogger(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        System.out.println("Service function name called: " + methodName);
        System.out.println("Return function type called: " + signature.getReturnType());
        if (joinPoint.getArgs()[0] instanceof String) {
            System.out.println("Login user: " + joinPoint.getArgs()[0].toString());
        }
    }

    @AfterReturning("execution(* com.example.deliveryapp.user.UserServiceImpl.*(..))")
    public void afterLogger(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        System.out.printf("Service function '%s' ended with success!%n", methodName);
        System.out.println("Return function type finished: " + signature.getReturnType());
    }
}
