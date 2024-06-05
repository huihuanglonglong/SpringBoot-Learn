package org.lyl.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.lyl.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

/**
 * 请求防重复提交构建类
 */
@Slf4j
@Configuration
public class ReqDeDupHelper {

    @Autowired
    @Qualifier("objectMapper")
    private ObjectMapper objectMapper;


    /**
     *
     * @param reqParam 原始数据
     * @param excludeKeys 需要提出参与MD5的数据
     * @return MD5(deDupParm)----Redis缓存
     * @throws IOException
     */
    public String deDupParamMD5(String reqParam, List<String> excludeKeys) throws IOException {

        TreeMap paramMap = objectMapper.readValue(reqParam, TreeMap.class);
        if (!CollectionUtils.isEmpty(excludeKeys)) {
            for (String excludeKey : excludeKeys) {
                paramMap.remove(excludeKey);
            }
        }

        String dupJsonStr = objectMapper.writeValueAsString(paramMap);
        String md5deDupStr = CommonUtil.jdkMD5(dupJsonStr);
        log.debug("md5deDupParam = {}, excludeKeys = {}", md5deDupStr, dupJsonStr);
        return md5deDupStr;
    }


}
