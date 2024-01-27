package org.lyl.config;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.lyl.common.util.CommonConst;
import org.lyl.config.LogMdcFilter;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Component
public class ThreadPoolMDCExecutor extends ThreadPoolTaskExecutor {

    @Resource(name = "asyncInvokeExecutor")
    private ThreadPoolTaskExecutor asyncExecutor;


    @Override
    public void execute(Runnable runnableTask) {
        asyncExecutor.execute(wrapRunnable(runnableTask, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> callableTask) {
        return asyncExecutor.submit(wrapCallable(callableTask, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return asyncExecutor.submit(wrapRunnable(task, MDC.getCopyOfContextMap()));
    }



    public static void setTraceIdIfAbsent() {
        if (StringUtils.isBlank(MDC.get(CommonConst.COMMON_TRACE_ID))) {
            MDC.put(CommonConst.COMMON_TRACE_ID, LogMdcFilter.buildMDCTraceId());
        }
    }

    // 封装需要调用的Callable
    public static <T> Callable<T> wrapCallable(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    // 封装需要调用的Runnable，将上下问和traceId传递到新的MDC, 当run完成之后清理
    public static Runnable wrapRunnable(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (MapUtils.isEmpty(context)) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }



}
