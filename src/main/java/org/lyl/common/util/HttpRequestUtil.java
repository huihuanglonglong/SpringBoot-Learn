package org.lyl.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class HttpRequestUtil {

    private static RestTemplate restTemplate = ApplicationContextUtil.getBean("restTemplate", RestTemplate.class);


    // 通过参数获取请求投
    public static HttpHeaders getHeaders(Map<String, String> headerParamMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (Objects.nonNull(headerParamMap) && headerParamMap.size() > 0) {
            headers.setAll(headerParamMap);
        }
        return headers;
    }


    // 通过post方式调用
    public static <T> T postInvoke(String reqUrl, HttpHeaders headers, String reqParamJson, ParameterizedTypeReference<T> rtnType) {
        HttpEntity<String> reqEntity = getStrHttpEntity(reqUrl, headers, reqParamJson);
        if (reqEntity == null) {
            return null;
        }
        return invokeByRtnType(reqUrl, HttpMethod.POST, reqEntity, rtnType);
    }

    // 通过Get方式调用
    public static <T> T getInvoke(String reqUrl, HttpHeaders headers, String reqParamJson, ParameterizedTypeReference<T> rtnType) {
        HttpEntity<String> reqEntity = getStrHttpEntity(reqUrl, headers, reqParamJson);
        if (reqEntity == null) {
            return null;
        }
        return invokeByRtnType(reqUrl, HttpMethod.GET, reqEntity, rtnType);
    }

    // 获取一个str类型的请求体
    private static HttpEntity<String> getStrHttpEntity (String reqUrl, HttpHeaders headers, String reqParamJson) {
        if (StringUtils.isBlank(reqUrl) || StringUtils.isBlank(reqParamJson)) {
            log.warn("post invoke param Empty, reqUrl = {}, reqParam = {}", reqUrl, reqParamJson);
            return null;
        }

        // 请求参数和header
        if (Objects.isNull(headers)) {
            headers = getHeaders(null);
        }
        HttpEntity<String> reqEntity = new HttpEntity<>(reqParamJson, headers);
        return reqEntity;
    }


    /**
     * restTemplate 调用底层实现
     *
     * @param reqUrl
     * @param reqMethod
     * @param reqEntity
     * @param rtnType
     * @param <T>
     * @return
     */
    public static <T> T invokeByRtnType(String reqUrl, HttpMethod reqMethod, HttpEntity<String> reqEntity, ParameterizedTypeReference<T> rtnType) {
        ResponseEntity<T> response = null;
        try {
            response = restTemplate.exchange(reqUrl, reqMethod, reqEntity, rtnType);
        } catch (Exception e) {
            log.error("invokeByRtnType reqUrl = {}, have error------>", reqUrl, e);
        }
        return getData(response, reqUrl);
    }


    private static <T> T getData(ResponseEntity<T> response, String reqUrl) {
        T data;
        if (Objects.isNull(response)) {
            log.error("invoke By restTemplate url = {}, have no response", reqUrl);
            return null;
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("invoke By restTemplate url = {}, http status = {}", reqUrl, response.getStatusCode());
            return null;
        }
        return  response.getBody();
    }









}
