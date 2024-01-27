package org.lyl.common.util;

import org.apache.commons.lang3.StringUtils;
import org.lyl.config.LogMdcFilter;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

public class ThreadMDCUtil {

    public static void setTraceIdIfAbsent() {
        if (StringUtils.isBlank(MDC.get(CommonConst.COMMON_TRACE_ID))) {
            MDC.put(CommonConst.COMMON_TRACE_ID, LogMdcFilter.buildMDCTraceId());
        }
    }


    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
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

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
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
