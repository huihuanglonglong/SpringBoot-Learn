package org.lyl.common.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JacksonUtil {

    private static ObjectMapper objectMapper = ApplicationContextUtil.getBean(ObjectMapper.class);

    /**
     * 获取集合类型的JavaType
     *
     * @param originalType 集合原始类型（Map，List，Set......）
     * @param parameterTypes 集合类型所需要的泛型参数类型()
     * @return
     */
    public static JavaType getCollectionType(Class<?> originalType, Class<?>... parameterTypes) {
        return objectMapper.getTypeFactory().constructParametricType(originalType, parameterTypes);
    }


    /**
     * 通过简单类型获取一个简单的JavaType
     *
     * @param clazz
     * @return
     */
    public static JavaType getSimpleType(Class<?> clazz) {
        return objectMapper.getTypeFactory().constructType(clazz);
    }


    /**
     * 通过一有的Javatype，构建一个复杂的多重泛型JavaType
     * 比如Response<Map<String, List<LayoutEntry>>>
     *
     * @param originalType 原始数据类型
     * @param complexType 需要的复杂泛型类型JavaType
     * @return
     */
    public static JavaType getComplexType(Class<?> originalType, JavaType complexType) {
        return objectMapper.getTypeFactory().constructParametricType(originalType, complexType);
    }




    // object ----> jsonStr
    public static String obj2Json(Object obj) {
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("jacksonUtil write str have error------>", e);
        }
        return jsonStr;
    }


    // jsonStr ----> object
    public static <T> T readJson2Obj(String jsonData, JavaType javaType) {
        T data = null;
        try {
            data = objectMapper.readValue(jsonData, javaType);
        } catch (IOException e) {
            log.error("readJson2Obj have error------>", e);
        }
        return data;
    }



}
