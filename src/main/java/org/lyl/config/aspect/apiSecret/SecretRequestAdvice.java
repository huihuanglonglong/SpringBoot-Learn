package org.lyl.config.aspect.apiSecret;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 *
 * API接口请求切面
 *
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class SecretRequestAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        //如果支持加密消息，进行消息解密。
        /*String httpBody;
        if (Boolean.TRUE.equals(SecretFilter.secretThreadLocal.get())) {
            httpBody = decryptBody(inputMessage);
        } else {
            httpBody = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
        }
        //返回处理后的消息体给messageConvert
        return new SecretHttpMessage(new ByteArrayInputStream(httpBody.getBytes()), inputMessage.getHeaders());*/
        return null;
    }

    /**
     * 解密消息体
     *
     * @param inputMessage 消息体
     * @return 明文
     */
    private String decryptBody(HttpInputMessage inputMessage) throws IOException {
        /*InputStream encryptStream = inputMessage.getBody();
        String requestBody = StreamUtils.copyToString(encryptStream, Charset.defaultCharset());
        // 验签过程
        HttpHeaders headers = inputMessage.getHeaders();
        if (CollectionUtils.isEmpty(headers.get("clientType"))
                || CollectionUtils.isEmpty(headers.get("timestamp"))
                || CollectionUtils.isEmpty(headers.get("salt"))
                || CollectionUtils.isEmpty(headers.get("signature"))) {
            throw new RuntimeException(SECRET_API_ERROR, "请求解密参数错误，clientType、timestamp、salt、signature等参数传递是否正确传递");
        }

        String timestamp = String.valueOf(Objects.requireNonNull(headers.get("timestamp")).get(0));
        String salt = String.valueOf(Objects.requireNonNull(headers.get("salt")).get(0));
        String signature = String.valueOf(Objects.requireNonNull(headers.get("signature")).get(0));
        String privateKey = SecretFilter.clientPrivateKeyThreadLocal.get();
        ReqSecret reqSecret = JSON.parseObject(requestBody, ReqSecret.class);
        String data = reqSecret.getData();
        String newSignature = "";
        if (!StringUtils.isEmpty(privateKey)) {
            newSignature = Md5Utils.genSignature(timestamp + salt + data + privateKey);
        }
        if (!newSignature.equals(signature)) {
            // 验签失败
            throw new ResultException(SECRET_API_ERROR, "验签失败，请确认加密方式是否正确");
        }

        try {
            String decrypt = EncryptUtils.aesDecrypt(data, privateKey);
            if (StringUtils.isEmpty(decrypt)) {
                decrypt = "{}";
            }
            return decrypt;
        } catch (Exception e) {
            log.error("error: ", e);
        }
        throw new ResultException(SECRET_API_ERROR, "解密失败");*/
        return null;
    }
}