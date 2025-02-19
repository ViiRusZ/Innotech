package ru.innotech.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SuccessLoggingAspect {

    @AfterReturning(pointcut = "@annotation(ru.innotech.aspect.annotation.AspectAfterReturning)", returning = "result")
    public void logAfterReturning(Object result) {
        log.info("Method executed successfully. Result: {}", result);
    }
}
