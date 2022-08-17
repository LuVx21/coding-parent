package org.luvx.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static org.luvx.common.util.TimeUtils.NORM_DATETIME_FORMAT;

@Slf4j
@UtilityClass
public final class JsonUtils {

    private static volatile ObjectMapper defaultMapper;

    private static volatile ObjectMapper supportNullValueMapper;

    private static volatile ObjectMapper snakeMapper;

    private static volatile ObjectMapper snakeSupportNullValueMapper;

    public static String toJson(Object obj) {
        return toJson(obj, false);
    }

    public static String toJsonSnake(Object obj) {
        return toJson(obj, false, true);
    }

    public static String toJson(Object obj, boolean supportNullValue) {
        return toJson(obj, supportNullValue, false);
    }

    public static String toJson(Object obj, boolean supportNullValue, boolean snake) {
        if (obj == null) {
            return null;
        }
        try {
            if (snake) {
                if (supportNullValue) {
                    return getSnakeNullValueMapper().writeValueAsString(obj);
                }
                return getSnakeMapper().writeValueAsString(obj);
            }

            if (supportNullValue) {
                return getNullValueMapper().writeValueAsString(obj);
            }
            return getDefaultMapper().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("[writeValueAsString]：" + e.getMessage(), e);
        }
        return null;
    }

    public static <T> T toObj(String json, Class<T> clazz) {
        return toObj(json, clazz, false);
    }

    public static <T> T toObjSnake(String json, Class<T> clazz) {
        return toObj(json, clazz, true);
    }

    public static <T> T toObj(String json, Class<T> clazz, boolean snake) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            if (snake) {
                return getSnakeMapper().readValue(json, clazz);
            }
            return getDefaultMapper().readValue(json, clazz);
        } catch (IOException e) {
            log.warn("[readValue]：" + e.getMessage(), e);
        }
        return null;

    }

    public static <T> T toObj(String json, TypeReference<T> typeRef) {
        return toObj(json, typeRef, false);
    }

    public static <T> T toObjSnake(String json, TypeReference<T> typeRef) {
        return toObj(json, typeRef, true);
    }

    public static <T> T toObj(String json, TypeReference<T> typeRef, boolean snake) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            if (snake) {
                return getSnakeMapper().readValue(json, typeRef);
            }
            return getDefaultMapper().readValue(json, typeRef);
        } catch (IOException e) {
            log.warn("[readValue]：" + e.getMessage(), e);
        }
        return null;
    }

    public static <T> T toObj(String json, Class<? extends Collection> collectionClass, Class<?> elementClass) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JavaType javaType = getDefaultMapper().getTypeFactory().constructCollectionType(collectionClass, elementClass);
            return (T) getDefaultMapper().readValue(json, javaType);
        } catch (IOException e) {
            log.warn("[toObj]" + e.getMessage(), e);
        }
        return null;
    }

    public static <T> T toObj(String json, Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JavaType javaType = getDefaultMapper().getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
            return (T) getDefaultMapper().readValue(json, javaType);
        } catch (IOException e) {
            log.warn("[toObj]" + e.getMessage(), e);
        }
        return null;
    }

    public static String formatJson(String json) {
        if (json == null) {
            return null;
        }
        try {
            Object obj = getDefaultMapper().readValue(json, Object.class);
            return getDefaultMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("[formatJson]：" + e.getMessage(), e);
        }
        return json;
    }

    private static ObjectMapper getDefaultMapper() {
        if (defaultMapper == null) {
            synchronized (JsonUtils.class) {
                if (defaultMapper == null) {
                    defaultMapper = new ObjectMapper();
                    setMapperCommonConfigure(defaultMapper, JsonInclude.Include.NON_NULL);
                }
            }
        }
        return defaultMapper;
    }

    private static ObjectMapper getNullValueMapper() {
        if (supportNullValueMapper == null) {
            synchronized (JsonUtils.class) {
                if (supportNullValueMapper == null) {
                    supportNullValueMapper = new ObjectMapper();
                    setMapperCommonConfigure(supportNullValueMapper, JsonInclude.Include.USE_DEFAULTS);
                }
            }
        }
        return supportNullValueMapper;
    }

    private static ObjectMapper getSnakeMapper() {
        if (snakeMapper == null) {
            synchronized (JsonUtils.class) {
                if (snakeMapper == null) {
                    snakeMapper = new ObjectMapper();
                    setMapperCommonConfigure(snakeMapper, JsonInclude.Include.NON_NULL);
                    snakeMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                }
            }
        }
        return snakeMapper;
    }

    private static ObjectMapper getSnakeNullValueMapper() {
        if (snakeSupportNullValueMapper == null) {
            synchronized (JsonUtils.class) {
                if (snakeSupportNullValueMapper == null) {
                    snakeSupportNullValueMapper = new ObjectMapper();
                    setMapperCommonConfigure(snakeSupportNullValueMapper, JsonInclude.Include.USE_DEFAULTS);
                    snakeSupportNullValueMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                }
            }
        }
        return snakeSupportNullValueMapper;
    }

    private static void setMapperCommonConfigure(ObjectMapper mapper, JsonInclude.Include include) {
        // 设置序列化忽略项
        mapper.setSerializationInclusion(include);
        // 设置时区
        mapper.setDateFormat(NORM_DATETIME_FORMAT);
        // 在序列化空的POJO类不抛出异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 在序列化自我引用则失败不抛出异常
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        //当出现 Java 类中未知的属性时不报错，而是忽略此 JSON 字段
        mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        //设置为false，表示： 在遇到未知属性的时候不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //反序列化时，遇到类名错误或者map中id找不到时是否报异常。默认为true
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        //反序列化时，遇到无法解析的对象id不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
        //默认情况下启用功能，以便在子类型属性丢失时抛出异常，关闭
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false);
    }
}
