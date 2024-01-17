package org.lyl.config;

import javax.servlet.Filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.lyl.common.util.CommonConst;
import org.lyl.common.util.DateTimeUtil;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author GUOSHAOHUA093
 * @Description 添加日志追踪id
 * @Date 2019-7-17 14:09
 **/
@Slf4j
@Component
@WebFilter(urlPatterns = "/*")
public class LogMdcFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("enter LogMdcFilter init method...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String traceId = buildMDCTraceId();
        MDC.put(CommonConst.COMMON_TRACE_ID, traceId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CommonConst.COMMON_TRACE_ID);
        }
    }

    @Override
    public void destroy() {
        log.info("enter LogMdcFilter destroy method...");
    }

    private String buildMDCTraceId() {
        String millis = DateTimeUtil.getCurrentTimeByFormat(DateTimeUtil.NUM_MILLIS_FORMAT);
        String uuId = UUID.randomUUID().toString().replace("-", "").substring(17);
        return millis + uuId;
    }

}
