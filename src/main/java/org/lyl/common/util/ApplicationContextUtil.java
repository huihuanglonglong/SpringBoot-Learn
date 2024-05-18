package org.lyl.common.util;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;


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

    /**
     * 配置文件获取配置项
     *
     * @param propertyKey
     * @param targetType
     * @param defaultVal
     * @param <T>
     * @return
     */
    public static <T> T getProperty(String propertyKey, Class<T> targetType, T defaultVal) {
        T property = context.getEnvironment().getProperty(propertyKey, targetType);
        return ObjectUtils.isEmpty(targetType)? defaultVal : property;
    }

    /**
     * 从Spring容器 通过父类泛型获取子类Bean
     *  Aclass extends AbsClass<XXXX>
     *
     * @param sourceClass
     * @param superGenericClass
     * @return
     */
    public static Object getBeanBySuperGenericType(Class<?> sourceClass, Class<?> superGenericClass) {
        final Object[] targetBeans = new Object[1];
        Map<String, ?> beansMap = getBeansByType(sourceClass);
        beansMap.forEach((beanName, bean) -> {
            Class<?> rawClass = ResolvableType.forClass(bean.getClass()).getSuperType().getGeneric(0).getRawClass();
            if (Objects.nonNull(rawClass) && superGenericClass.getName().equals(rawClass.getName())) {
                targetBeans[0] = bean;
            }
        });
        return targetBeans[0];
    }


    /******************************* 代理相关 *******************************************/

    /**
     * 通过代理对象获取 目标对象---方案一
     *
     * @param proxy
     * @param <T>
     * @return
     */
    public static <T> T getAopProxySourceBean(T proxy) {
        return (T) AopProxyUtils.getSingletonTarget(proxy);
    }


    /**
     * 通过代理对象获取 目标对象---方案二
     * @param proxy 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        //不是代理对象
        if(!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        return AopUtils.isJdkDynamicProxy(proxy)? getJdkDynamicProxyTargetObject(proxy) : getCglibProxyTargetObject(proxy);
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field proxyCglibField = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        proxyCglibField.setAccessible(true);
        Object dynamicAdvisedInterceptor = proxyCglibField.get(proxy);

        Field advisedField = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advisedField.setAccessible(true);
        Object targetObj = ((AdvisedSupport)advisedField.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        advisedField.setAccessible(false);
        return targetObj;
    }


    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advisedField = aopProxy.getClass().getDeclaredField("advised");
        advisedField.setAccessible(true);

        Object target = ((AdvisedSupport)advisedField.get(aopProxy)).getTargetSource().getTarget();
        advisedField.setAccessible(false);
        return target;
    }


}
