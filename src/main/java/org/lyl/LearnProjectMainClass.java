package org.lyl;

import org.lyl.config.BeanLifeConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


//@MapperScan()
@EnableScheduling
@EnableAsync
@ServletComponentScan
@EnableWebMvc
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class LearnProjectMainClass {

    private static Logger logger = LoggerFactory.getLogger(LearnProjectMainClass.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(LearnProjectMainClass.class, args);
        BeanLifeConfig bean = run.getBean(BeanLifeConfig.class);
        logger.info("BeanLifeConfig = {}", bean);
    }

}
