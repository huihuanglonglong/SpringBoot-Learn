package org.lyl.common.sensitive;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.lyl.common.util.ApplicationContextUtil;
import org.lyl.common.util.JacksonUtil;
import org.lyl.common.util.CommonUtil;
import org.slf4j.helpers.MessageFormatter;

import java.util.Objects;

public class MessageSensitiveConverter extends MessageConverter {

    private ObjectMapper logObjectMapper = null;

    public String convert(ILoggingEvent event) {
        if (Objects.isNull(event)) {
            return StringUtils.EMPTY;
        }
        if (Objects.nonNull(event.getArgumentArray())) {
            argsMask(event.getArgumentArray());
        }
        return MessageFormatter.arrayFormat(event.getMessage(), event.getArgumentArray()).getMessage();
    }


    private void argsMask(Object[] logArgs) {
        if (Objects.isNull(logArgs) || logArgs.length == 0) {
            return;
        }
        for (int i = 0; i < logArgs.length; i++) {
            Object logArg = logArgs[i];
            if (Objects.isNull(logArg) || (logArg instanceof Throwable) || CommonUtil.isBasicClazz(logArg.getClass())) {
                continue;
            }
            // 按照指定的方式序列化
            logArgs[i] = JacksonUtil.obj2Json(getLogObjectMapper(), logArg);
        }
    }


    private ObjectMapper getLogObjectMapper() {
        if (Objects.isNull(this.logObjectMapper)) {
            this.logObjectMapper = ApplicationContextUtil.getBean("logObjectMapper", ObjectMapper.class);
        }
        return this.logObjectMapper;
    }

}
