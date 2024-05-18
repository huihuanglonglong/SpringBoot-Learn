package org.lyl.listener;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import org.lyl.common.sensitive.MessageSensitiveConverter;
import org.lyl.common.util.ReflectionUtil;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LogBackSensitiveConvertEventListener extends LoggingApplicationListener {

    @Override
    public int getOrder() {
        return super.getOrder() + 1;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            resetMessageConverterForLogContext();
        }
    }

    private void resetMessageConverterForLogContext() {
        LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();

        Logger logger = ReflectionUtil.getFieldValueByName(loggerContext, "root");
        AppenderAttachableImpl<ILoggingEvent> aai = ReflectionUtil.getFieldValueByName(logger, "aai");

        List<Encoder<ILoggingEvent>> appenderList = ReflectionUtil.getFieldValueByName(aai, "appenderList");
        resetMessageConverter(((ConsoleAppender<ILoggingEvent>)appenderList.get(0)).getEncoder());
        resetMessageConverter(((RollingFileAppender<ILoggingEvent>)appenderList.get(1)).getEncoder());
    }

    private void resetMessageConverter(Encoder<ILoggingEvent> encoder) {
        PatternLayout layout = ReflectionUtil.getFieldValueByName(encoder, "layout");
        Converter<ILoggingEvent> headConverter = ReflectionUtil.getFieldValueByName(layout, "head");

        while (Objects.nonNull(headConverter)) {
            if (Objects.nonNull(headConverter.getNext()) && (headConverter.getNext() instanceof MessageConverter)) {

                Converter<ILoggingEvent> nextNode = Optional.of(headConverter.getNext()).map(Converter::getNext).orElse(null);
                MessageSensitiveConverter customerConverter = new MessageSensitiveConverter();

                customerConverter.start();
                customerConverter.setNext(nextNode);

                ReflectionUtil.setFieldByValue(headConverter, "next", null);
                headConverter.setNext(customerConverter);
                break;
            }
            headConverter = headConverter.getNext();
        }
    }

}
