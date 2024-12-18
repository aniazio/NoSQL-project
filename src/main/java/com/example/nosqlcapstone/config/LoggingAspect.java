package com.example.nosqlcapstone.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.example.nosqlcapstone.controller..*)")
    public void inController() {}

    @Before("com.example.nosqlcapstone.config.LoggingAspect.inController()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log.debug("Before method: {}, Arguments: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterReturning(value = "com.example.nosqlcapstone.config.LoggingAspect.inController()", returning = "retVal")
    public void logAfterMethod(JoinPoint joinPoint, Object retVal) {
        log.debug("After method: {}, Arguments: {}, Returning: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs(), retVal);
    }

}
