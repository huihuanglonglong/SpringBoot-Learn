package org.lyl.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class ReflectionUtil {


    public static <T> T getFieldValueByName(Object target, String fieldName) {
        T fieldValue = null;
        Field targetField = findFiledByName(target, fieldName);
        if (Objects.nonNull(targetField)) {
            fieldValue = getFieldValue(targetField, target);
        }
        return fieldValue;
    }


    public static Field findFiledByName(Object target, String fieldName) {
        if (Objects.isNull(target) || StringUtils.isBlank(fieldName)) {
            return null;
        }
        Class<?> sourceClass = target.getClass();
        Field targetField = null;

        while (Objects.nonNull(sourceClass)) {
            targetField = Arrays.stream(sourceClass.getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName)).findFirst().orElse(null);
            if (Objects.nonNull(targetField)) {
                break;
            }
            sourceClass = sourceClass.getSuperclass();
        }
        return targetField;
    }


    public static void setFieldByValue(Object target, String fieldName, Object fieldVal) {
        Field targetField = findFiledByName(target, fieldName);
        setFieldValue(target, targetField, fieldVal);
    }



    public static <T> T getFieldValue(Field targetField, Object targetObj) {
        Object fieldValue = null;
        targetField.setAccessible(Boolean.TRUE);
        try {
            fieldValue = targetField.get(targetObj);
        } catch (Exception e) {
            log.error("getFieldValue fieldName = {} by reflect have error------>", targetField.getName(), e);
        }
        targetField.setAccessible(Boolean.FALSE);
        return (T)fieldValue;
    }

    public static void setFieldValue(Object target, Field targetField, Object value) {
        targetField.setAccessible(Boolean.TRUE);
        try {
            targetField.set(target, value);
        } catch (Exception e) {
            log.error("setFieldValue fieldName = {} by reflect have error------>", targetField.getName(), e);
        }
    }


}
