package ru.innotech.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {

    @AfterThrowing(pointcut = "@annotation(ru.innotech.aspect.annotation.AspectAfterThrowing)", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Error in method: {}, with args {}, {}: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs(),
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }
}