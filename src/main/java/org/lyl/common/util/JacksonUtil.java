package org.lyl.common.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER = ApplicationContextUtil.getBean(ObjectMapper.class);

    /**
     * 获取集合类型的JavaType
     *
     * @param originalType 集合原始类型（Map，List，Set......）
     * @param parameterTypes 集合类型所需要的泛型参数类型()
     * @return
     */
    public static JavaType getCollectionType(Class<?> originalType, Class<?>... parameterTypes) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(originalType, parameterTypes);
    }


    /**
     * 通过简单类型获取一个简单的JavaType
     *
     * @param clazz
     * @return
     */
    public static JavaType getSimpleType(Class<?> clazz) {
        return OBJECT_MAPPER.getTypeFactory().constructType(clazz);
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
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(originalType, complexType);
    }

    /**
     * JavaType 转 Spring.TypeReference
     *
     * @param javaType
     * @param <T>
     * @return
     */
    public static <T> ParameterizedTypeReference<T> getRtnTyp(JavaType javaType) {
        return ParameterizedTypeReference.forType(javaType);
    }

    /**
     * JavaType在RestTemplate中的使用方式
     *
     * JavaType rowType = getCollectionType(List.class, String.class);
     * HttpRequestUtil.postInvoke("url", new HttpHeaders(), StringUtils.EMPTY, getRtnType(rowType));
     *
     *
     * @param obj
     * @return
     */



    // object ----> jsonStr
    public static String obj2JsonByPrimary(Object obj) {
        String jsonStr = null;
        try {
            jsonStr = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("obj2JsonByPrimary write str have error------>", e);
        }
        return jsonStr;
    }

    public static String obj2Json(ObjectMapper objectMapper, Object obj) {
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
            data = OBJECT_MAPPER.readValue(jsonData, javaType);
        } catch (IOException e) {
            log.error("readJson2Obj have error------>", e);
        }
        return data;
    }



    public static <T> T map2Object(Map<String, Object> dataMap, Class<T> clazz) throws Exception {
        String jsonStr = OBJECT_MAPPER.writeValueAsString(dataMap);
        return OBJECT_MAPPER.readValue(jsonStr, clazz);
    }


}
