package org.luvx.coding.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.luvx.coding.common.json.JsonAbbr;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.USE_DEFAULTS;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static org.luvx.coding.common.util.TimeUtils.NORM_DATETIME_FORMAT;

@Slf4j
@UtilityClass
public final class JsonUtils {

    private static final String EMPTY_JSON       = "{}";
    private static final String EMPTY_ARRAY_JSON = "[]";

    private static volatile ObjectMapper defaultMapper;
    private static volatile ObjectMapper supportNullValueMapper;

    private static volatile ObjectMapper snakeMapper;
    private static volatile ObjectMapper snakeSupportNullValueMapper;

    public static final TypeReference<Map<String, Object>> TYPE_REFERENCE_MAP_STRING_OBJECT = new TypeReference<>() {
    };
    public static final TypeReference<Map<String, String>> TYPE_REFERENCE_MAP_STRING_STRING = new TypeReference<>() {
    };

    public static String toJson(Object obj) {
        return toJson(obj, false, false);
    }

    public static String toJsonSnake(Object obj) {
        return toJson(obj, false, true);
    }

    public static String toJson(Object obj, boolean supportNullValue, boolean snake) {
        if (obj == null) {
            return null;
        }
        try {
            if (snake) {
                return (supportNullValue ? getSnakeNullValueMapper() : getSnakeMapper())
                        .writeValueAsString(obj);
            }
            return (supportNullValue ? getNullValueMapper() : getDefaultMapper())
                    .writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("[writeValueAsString]：{}", e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, false);
    }

    public static <T> T fromJsonSnake(String json, Class<T> clazz) {
        return fromJson(json, clazz, true);
    }

    public static <T> T fromJson(String json, Class<T> clazz, boolean snake) {
        if (!isValid(json)) {
            return null;
        }
        try {
            return (snake ? getSnakeMapper() : getDefaultMapper()).readValue(json, clazz);
        } catch (IOException e) {
            log.warn("[readValue]：{}", e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        return fromJson(json, typeRef, false);
    }

    public static <T> T fromJsonSnake(String json, TypeReference<T> typeRef) {
        return fromJson(json, typeRef, true);
    }

    public static <T> T fromJson(String json, TypeReference<T> typeRef, boolean snake) {
        if (!isValid(json)) {
            return null;
        }
        try {
            return (snake ? getSnakeMapper() : getDefaultMapper()).readValue(json, typeRef);
        } catch (IOException e) {
            log.warn("[readValue]：{}", e.getMessage(), e);
        }
        return null;
    }

    public static <T extends List<E>, E> List<E> parseArray(String json, Class<T> listClass, Class<E> elementClass) {
        if (!isValid(json)) {
            return Collections.emptyList();
        }
        try {
            JavaType javaType = getDefaultMapper().getTypeFactory().constructCollectionType(listClass, elementClass);
            return getDefaultMapper().readValue(json, javaType);
        } catch (IOException e) {
            log.warn("[parseArray]:{}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public static <M extends Map<String, V>, V> M fromJson(String json,
                                                           Class<M> mapClass, Class<V> vClass
    ) {
        if (!isValid(json)) {
            json = EMPTY_JSON;
        }
        try {
            MapType javaType = getDefaultMapper().getTypeFactory().constructMapType(mapClass, String.class, vClass);
            return getDefaultMapper().readValue(json, javaType);
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static Map<String, Object> fromJson(String json) {
        return fromJson(json, Map.class, Object.class);
    }

    public static String toPrettyJson(String json) {
        if (json == null) {
            return null;
        }
        try {
            Object obj = getDefaultMapper().readValue(json, Object.class);
            return getDefaultMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("[formatJson]：{}", e.getMessage(), e);
        }
        return json;
    }

    private static ObjectMapper getDefaultMapper() {
        if (defaultMapper == null) {
            synchronized (JsonUtils.class) {
                if (defaultMapper == null) {
                    defaultMapper = new ObjectMapper();
                    setMapperCommonConfigure(defaultMapper, NON_NULL);
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
                    setMapperCommonConfigure(supportNullValueMapper, USE_DEFAULTS);
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
                    setMapperCommonConfigure(snakeMapper, NON_NULL);
                    snakeMapper.setPropertyNamingStrategy(SNAKE_CASE);
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
                    setMapperCommonConfigure(snakeSupportNullValueMapper, USE_DEFAULTS);
                    snakeSupportNullValueMapper.setPropertyNamingStrategy(SNAKE_CASE);
                }
            }
        }
        return snakeSupportNullValueMapper;
    }

    private static RuntimeException wrapException(IOException e) {
        if (e instanceof JsonProcessingException) {
            return new UncheckedJsonProcessingException((JsonProcessingException) e);
        } else {
            return new UncheckedIOException(e);
        }
    }

    public class UncheckedJsonProcessingException extends UncheckedIOException {
        public UncheckedJsonProcessingException(JsonProcessingException cause) {
            super(cause.getMessage(), cause);
        }
    }

    private static void setMapperCommonConfigure(ObjectMapper mapper, Include include) {
        // 设置序列化忽略项
        mapper.setSerializationInclusion(include);
        // 设置时区
        mapper.setDateFormat(NORM_DATETIME_FORMAT);
        // 在序列化空的POJO类不抛出异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 在序列化自我引用则失败不抛出异常
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        // 当出现 Java 类中未知的属性时不报错，而是忽略此 JSON 字段
        mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        // 设置为false，表示： 在遇到未知属性的时候不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 反序列化时，遇到类名错误或者map中id找不到时是否报异常。默认为true
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        // 反序列化时，遇到无法解析的对象id不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
        // 默认情况下启用功能，以便在子类型属性丢失时抛出异常，关闭
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false);
    }

    public static Triple<Set<String>, Set<String>, Map<String, Entry<Object, Object>>> diff(String json1, String json2) {
        Map<String, Object> map1 = JsonAbbr.jsonAbbr(json1);
        Map<String, Object> map2 = JsonAbbr.jsonAbbr(json2);

        Set<String> s1 = Sets.newHashSet(), s2 = Sets.newHashSet();
        Map<String, Entry<Object, Object>> diff = Maps.newHashMap();
        for (Entry<String, Object> entry : map1.entrySet()) {
            String key = entry.getKey();
            if (map2.containsKey(key)) {
                Object value = entry.getValue();
                Object o = map2.get(key);
                if (!Objects.equals(value, o)) {
                    diff.put(key, new SimpleEntry<>(value, o));
                }
            } else {
                s1.add(key);
            }
        }
        for (Entry<String, Object> entry : map2.entrySet()) {
            String key = entry.getKey();
            if (!map1.containsKey(key)) {
                s2.add(key);
            }
        }

        return Triple.of(s1, s2, diff);
    }

    private boolean isValid(String json) {
        return StringUtils.isNotBlank(json)
                && StringUtils.length(json) >= 2
                && !"\"\"".equals(json);
    }
}
