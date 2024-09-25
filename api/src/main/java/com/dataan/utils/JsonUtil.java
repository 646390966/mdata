package com.dataan.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhan bing liang
 * @date 2023/5/17 13:54
 */
public class JsonUtil {

    private JsonUtil() {

    }

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static <T> T readValue(String json, Class<T> t) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (t == String.class) {
            return (T)json;
        }
        try {
            return OBJECT_MAPPER.readValue(json, t);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static <T> T readValue(String json, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T convertValue(Object obj, TypeReference<T> valueTypeRef) {
        if (String.class.isInstance(obj)) {
            return readValue((String)obj,valueTypeRef);
        }
        return OBJECT_MAPPER.convertValue(obj, valueTypeRef);
    }

    public static <T> T convertValue(Object obj, Class<T> clazz) {
        if (String.class.isInstance(obj)) {
            return readValue((String)obj,clazz);
        }
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }

    public static String writeValueAsString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
