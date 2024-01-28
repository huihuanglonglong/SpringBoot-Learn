package org.lyl.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lyl.common.util.encrypt.AesEncryptUtil;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ConfigPropertyDecryptListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String DECRYPT_PASS = System.getProperty("config.property.pass");

    private static final int DECRYPT_STAT_INDEX = 4;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();
        MutablePropertySources propertySources = env.getPropertySources();

        // 过滤出配置文件
        List<OriginTrackedMapPropertySource> configSources = propertySources.stream()
            .filter(source -> source instanceof OriginTrackedMapPropertySource)
            .map(source -> (OriginTrackedMapPropertySource) source).collect(Collectors.toList());

        // 因为OriginTrackedMapPropertySource 里面的source是UnModifiedMap,只能重新构建，在覆盖
        List<OriginTrackedMapPropertySource> decryptSources = Lists.newArrayList();
        for (OriginTrackedMapPropertySource originalSource : configSources) {

            // 从原始配置文件Map中每一项过滤出需要解密的数据
            Map<String, Object> decryptConfigMap = Maps.newHashMap();
            originalSource.getSource().forEach((proKey, proValue) -> {
                String proValStr = String.valueOf(proValue);
                if (StringUtils.isNotBlank(proValStr) && proValStr.startsWith("ENC(") && proValStr.endsWith("]")) {
                    proValStr = decryptProperty(proKey, proValStr.substring(DECRYPT_STAT_INDEX, proValStr.length()-1));
                }
                decryptConfigMap.put(proKey, proValStr);
            });

            // 通过解密后的配置decryptConfigMap, 构建新的MapPropertySource
            OriginTrackedMapPropertySource decryPropertySource = new OriginTrackedMapPropertySource(originalSource.getName(), Collections.unmodifiableMap(decryptConfigMap));
            decryptSources.add(decryPropertySource);
        }

        // 通过配置文件名，重新设置解密后的配置
        decryptSources.forEach(decryPropertySource -> {
            propertySources.remove(decryPropertySource.getName());
            propertySources.addLast(decryPropertySource);
        });

    }


    private String decryptProperty(String proKey, String encryptText) {
        String decryptResult = StringUtils.EMPTY;
        try {
            decryptResult = AesEncryptUtil.decryptCBCModeDynamicSalt(DECRYPT_PASS, encryptText);
        } catch (Exception e) {
            log.error("decryptProperty keyName = {}", proKey);
        }
        return decryptResult;
    }
}
