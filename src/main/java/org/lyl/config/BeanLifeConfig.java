package org.lyl.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class BeanLifeConfig implements ApplicationContextAware, InitializingBean, DisposableBean {

    private ApplicationContext context;

    @Autowired
    @Qualifier("asyncInvokeExecutor")
    private TaskExecutor asyncInvokeExecutor;

    @PostConstruct
    public void postConstructMethod() {
        log.info("BeanLifeConfig execute postConstructMethod()......");
    }

    @PreDestroy
    public void annotationDestroy() {
        log.info("BeanLifeConfig execute annotationDestroy()......");
    }

    // InitializingBean
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("BeanLifeConfig execute InitializingBean.afterPropertiesSet()......");
    }

    // DisposableBean
    @Override
    public void destroy() throws Exception {
        log.info("BeanLifeConfig execute isposableBean.destroy()......");
    }
    
    // ApplicationContextAware
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        log.info("BeanLifeConfig execute ApplicationContextAware.setApplicationContext().....");
    }

}
