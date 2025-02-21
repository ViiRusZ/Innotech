package ru.innotech.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodLoggingAspect {

    @Before("@annotation(ru.innotech.aspect.annotation.LoggingAspectBeforeMethod)")
    public void loggingBeforeMethod(JoinPoint joinPoint) {
        log.info("Starting method {}, with params {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @After("@annotation(ru.innotech.aspect.annotation.LoggingAspectAfterMethod)")
    public void loggingAfterMethod(JoinPoint joinPoint) {
        log.info("Ending method {}", joinPoint.getSignature());
    }
}