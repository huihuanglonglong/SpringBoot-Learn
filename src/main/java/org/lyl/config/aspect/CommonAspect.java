package org.lyl.config.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.lyl.common.annotation.LogTracing;
import org.lyl.common.util.CommonConst;
import org.lyl.config.LogMdcFilter;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * 同一个漆面中，通知的执行顺讯如下
 * try{
 *     try{
 *         //@Around start
 *         //@Before
 *         method.invoke(..);
 *         //@Around end
 *     }finally{
 *         //@After
 *     }
 *     //@AfterReturning
 * }catch(){
 *     //@AfterThrowing
 * }
 *
 *
 */
@Aspect
@Component
@Slf4j
public class CommonAspect {

    /**
     * 日志跟踪切点，绑定注解参数
     *
     * @param logTraceAnnotation
     */
    @Pointcut(value = "@annotation(logTraceAnnotation)")
    private void tracePoint(LogTracing logTraceAnnotation) {
        log.info("execute trace pointcut...");
    }

    /**
     * 1、Around
     * 在目标方法的前后自定义一下操作
     *
     * @param joinPoint
     * @param logTraceAnnotation
     * @return
     * @throws Throwable
     */
    @Around(value = "tracePoint(logTraceAnnotation)")
    public Object traceMethodAround(ProceedingJoinPoint joinPoint, LogTracing logTraceAnnotation) throws Throwable {
        Object result;
        try {
            MDC.put(CommonConst.COMMON_TRACE_ID, LogMdcFilter.buildMDCTraceId());
            result = joinPoint.proceed();
        } finally {
            MDC.clear();
        }
        return result;
    }


    /**
     * 2、Before 一般都是对参数进行调整封装
     *
     * @param joinPoint 连接点，必须为方法的第一个参数
     * @param logTraceAnnotation 目标方法上的切入注解
     */
    @Before(value = "tracePoint(logTraceAnnotation)")
    public void beforeTraceMethod(JoinPoint joinPoint, LogTracing logTraceAnnotation) {
        //目标类的全限定名称
        String className=joinPoint.getTarget().getClass().getName();
        String methodName=joinPoint.getSignature().getName();
        //方法入参
        Object[] paramArr=joinPoint.getArgs();

        //注解信息：@
        String title=logTraceAnnotation.value();
        log.info("before execute methodName = {}, params = {}", methodName, paramArr);
    }


    /**
     * 3、after, 原生方法执行完毕之后，不管是否有异常都会执行
     *
     * @param joinPoint
     * @param logTraceAnnotation
     * @return
     */
    @After(value = "tracePoint(logTraceAnnotation)")
    public Object afterTraceMethod(JoinPoint joinPoint, LogTracing logTraceAnnotation) {
        String methodName = joinPoint.getSignature().getName();
        log.info("after execute methodName = {}", methodName);
        return joinPoint.getThis();
    }

    /**
     * 4、AfterReturning
     *
     * @param joinPoint
     * @param logTraceAnnotation
     * @param result
     * @return
     */
    @AfterReturning(value = "tracePoint(logTraceAnnotation)", returning = "result")
    public Object afterTraceReturning(JoinPoint joinPoint, LogTracing logTraceAnnotation, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("afterReturning execute methodName = {}", methodName);
        return result;
    }


    /**
     * 5、afterThrowing
     *
     *
     * @return
     */
    @AfterThrowing(value = "@annotation(logTraceAnnotation)", throwing = "e")
    public Object errorHappened(JoinPoint joinPoint, LogTracing logTraceAnnotation, Exception e) {
        String methodName = joinPoint.getSignature().getName();
        log.error("AfterThrowing execute methodName = {}", methodName);
        return null;
    }

}
