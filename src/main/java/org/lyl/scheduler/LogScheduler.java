package org.lyl.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.lyl.common.util.DateTimeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogScheduler {

    private static final String logStr = "[main: ] INFO  o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker.postProcessAfterInitialization:330 - Bean 'threadPoolConfig' of type [org.lyl.config.threadPool.ThreadPoolConfig$$EnhancerBySpringCGLIB$$c4c3ac76] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)";

    //@Scheduled(cron = "0 0/1 * * * ?")
    public void testLogClean() {
        for (int i = 0; i < 1000000; i++) {
            String currentTime = DateTimeUtil.getCurrentStandardMillisTime();
            log.info("test logStr currentTime = {}, loop = {}, testLog = {}", currentTime, i, logStr);
        }
    }

}
