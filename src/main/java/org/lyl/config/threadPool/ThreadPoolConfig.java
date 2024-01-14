package org.lyl.config.threadPool;

import lombok.extern.slf4j.Slf4j;
import org.omg.SendingContext.RunTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class ThreadPoolConfig {

    private static final int CORE_SIZE = 2 * Runtime.getRuntime().availableProcessors();

    @Primary
    @Bean("asyncInvokeExecutor")
    public TaskExecutor taskExecutorConfig() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(CORE_SIZE);
        // 设置最大线程数
        executor.setMaxPoolSize(4 * CORE_SIZE);
        // 设置队列容量
        executor.setQueueCapacity(2000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称, 在设置了线程工厂的场景无效
        executor.setThreadNamePrefix("org-asyncExecutor---");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池, 线程池不关闭
        executor.setWaitForTasksToCompleteOnShutdown(false);
        // 自定义线程工厂用于异常处理
        executor.setThreadFactory(customThreadFactory());
        return executor;
    }



    private ThreadFactory customThreadFactory() {
        ThreadFactory factory = (Runnable r) -> {
            Thread t = new Thread(r);
            Thread.setDefaultUncaughtExceptionHandler((Thread thread, Throwable e) -> {
                log.error("asyncInvokeExecutor execute have error------>", e);
            });
            return t;
        };
        return factory;
    }


}
