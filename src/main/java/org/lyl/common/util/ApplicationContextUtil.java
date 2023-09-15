package org.lyl.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 *
 * Spring容器工具
 *
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;




    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        // org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext
    }


    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }


    public static <T> Map<String, T> getBeansByType(Class<T> clazz) {
        Map<String, T> beanMap = context.getBeansOfType(clazz);
        return beanMap;
    }



}
