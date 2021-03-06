package com.alibaba.dubbo.common.serialize.support.json;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.jahhan.common.extension.utils.ExtensionUtil;
import net.jahhan.spi.common.JacksonObjectMapperProvider;

/**
 * @author dylan
 */
public class Jackson {
    private static Logger logger = LoggerFactory.getLogger(Jackson.class);
    private static ObjectMapper objectMapper;
    
    private static JacksonObjectMapperProvider getJacksonProvider() {
        return ExtensionUtil.getExtension(JacksonObjectMapperProvider.class);
    }

    /**
     * 获取object mapper
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            JacksonObjectMapperProvider jacksonObjectMapperProvider = getJacksonProvider();
            if (jacksonObjectMapperProvider != null) {
                objectMapper = jacksonObjectMapperProvider.getObjectMapper();
            }
        }
        if (objectMapper == null) {
            logger.warn("load objectMapper failed, use default config.");
            buildDefaultObjectMapper();
        }
        return objectMapper;
    }

    private static synchronized void buildDefaultObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//            objectMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setTimeZone(TimeZone.getDefault());
    }
}
