package org.lyl.config.aspect.apiSecret;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.awt.*;

@Slf4j
@ControllerAdvice
public class SecretResponseAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    // 参考：https://mp.weixin.qq.com/s/b5l3-v2R1SzMVh4bDwNVmg
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 判断是否需要加密
        /*Boolean respSecret = SecretFilter.secretThreadLocal.get();
        String secretKey = SecretFilter.clientPrivateKeyThreadLocal.get();
        // 清理本地缓存
        SecretFilter.secretThreadLocal.remove();
        SecretFilter.clientPrivateKeyThreadLocal.remove();
        if (null != respSecret && respSecret) {
            if (o instanceof ResponseBasic) {
                // 外层加密级异常
                if (SECRET_API_ERROR == ((ResponseBasic) o).getCode()) {
                    return SecretResponseBasic.fail(((ResponseBasic) o).getCode(), ((ResponseBasic) o).getData(), ((ResponseBasic) o).getMsg());
                }
                // 业务逻辑
                try {
                    String data = EncryptUtils.aesEncrypt(JSON.toJSONString(o), secretKey);
                    // 增加签名
                    long timestamp = System.currentTimeMillis() / 1000;
                    int salt = EncryptUtils.genSalt();
                    String dataNew = timestamp + "" + salt + "" + data + secretKey;
                    String newSignature = Md5Utils.genSignature(dataNew);
                    return SecretResponseBasic.success(data, timestamp, salt, newSignature);
                } catch (Exception e) {
                    logger.error("beforeBodyWrite error:", e);
                    return SecretResponseBasic.fail(SECRET_API_ERROR, "", "服务端处理结果数据异常");
                }
            }
        }*/
        return o;
    }
}
