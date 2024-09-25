package com.dataan.interceptor;

import com.dataan.annotation.SqlArgument;
import com.dataan.db.FieldUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author zhan bing liang
 * @date 2024/6/25 15:53
 */
@Aspect
@Component
public class ControllerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    /**
     *  定义切点，匹配controller包下所有方法执行
     */
    @Pointcut("execution(* com.dataan.controller..*.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object  aroundControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        validSqlArgument(joinPoint);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try{
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            LOGGER.info("调用{}耗时{}秒",joinPoint.getSignature().getName(),stopWatch.getTotalTimeSeconds());
        }
    }

    private void validSqlArgument(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();
        for(int i=0;i<annotations.length;i++) {
            for(int j=0;j<annotations[i].length;j++) {
                Annotation annotation = annotations[i][j];
                if (annotation.annotationType().equals(SqlArgument.class)) {
                    FieldUtil.judgeSqlInjection(String.valueOf(args[i]));
                }
            }
        }
    }





}
