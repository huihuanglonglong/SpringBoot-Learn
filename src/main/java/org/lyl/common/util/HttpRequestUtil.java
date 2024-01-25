package org.lyl.common.util;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
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
    public static <T> T postInvoke(String reqUrl, HttpHeaders headers, Map<String, Object> reqParam, ParameterizedTypeReference<T> rtnType) {
        HttpEntity<String> reqEntity = getHttpEntity(reqUrl, headers, reqParam);
        if (reqEntity == null) {
            return null;
        }
        return invokeByRtnType(reqUrl, HttpMethod.POST, reqEntity, null, rtnType);
    }

    // 通过Get方式调用
    public static <T> T getInvoke(String reqUrl, HttpHeaders headers, Map<String, Object> reqParams, ParameterizedTypeReference<T> rtnType) {
        HttpEntity<String> reqEntity = getHttpEntity(reqUrl, headers, null);
        if (reqEntity == null) {
            return null;
        }
        reqUrl = reBuildReqUrl(reqUrl, reqParams);
        return invokeByRtnType(reqUrl, HttpMethod.GET, reqEntity, reqParams, rtnType);
    }

    //Get请求涉及到路径参数拼接，但是参数值如果是json格式就存在“{}”两个字符的和路径参数模板冲突，所以必须要构建Uri模板
    private static String reBuildReqUrl(String reqUrl, Map<String, Object> reqParam) {
        if (MapUtils.isNotEmpty(reqParam)) {
            StringBuilder sb = new StringBuilder(reqUrl + "?");
            reqParam.keySet().forEach(paramKey ->
                    sb.append(paramKey).append("=").append("{").append(paramKey).append("}").append("&"));
            reqUrl = sb.substring(0, sb.length()-1);
        }
        return reqUrl;
    }

    // 获取一个str类型的请求体
    private static HttpEntity<String> getHttpEntity(String reqUrl, HttpHeaders headers, Map<String, Object> reqParams) {
        if (StringUtils.isBlank(reqUrl) && MapUtils.isEmpty(reqParams)) {
            log.warn("post invoke param Empty, reqUrl = {}, reqParam = {}", reqUrl, reqParams);
            return null;
        }

        // 请求参数和header
        if (Objects.isNull(headers)) {
            headers = getHeaders(null);
        }
        HttpEntity<String> reqEntity = new HttpEntity<>(JSON.toJSONString(reqParams), headers);
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
    public static <T> T invokeByRtnType(String reqUrl, HttpMethod reqMethod, HttpEntity<String> reqEntity,
                                        Map<String, Object> uriParams, ParameterizedTypeReference<T> rtnType) {
        ResponseEntity<T> response = null;
        try {
            response = restTemplate.exchange(reqUrl, reqMethod, reqEntity, rtnType, uriParams);
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
