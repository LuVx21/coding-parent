package org.luvx.coding.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.Collections.emptyMap;

@Slf4j
public class MoreBeanUtils {
    /**
     * 通过字段名获取方法数组
     *
     * @param beanClass      Class<?>
     * @param fieldNameArray 要输出的所有字段名数组
     * @return Method[]
     */
    public static Method[] getMethods(Class<?> beanClass, String[] fieldNameArray) {
        Method[] methodArray = new Method[fieldNameArray.length];

        String methodName;
        String fieldName;
        for (int i = 0; i < fieldNameArray.length; i++) {
            Method method = null;
            fieldName = fieldNameArray[i];
            methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                method = beanClass.getMethod("get" + methodName, null);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                try {
                    method = beanClass.getMethod("is" + methodName, null);
                } catch (SecurityException e1) {
                    e1.printStackTrace();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                }
            }
            methodArray[i] = method;
        }

        return methodArray;
    }


    /**
     * 对象转list, 按顺序存在字段名和字段值
     */
    public static List<Pair<String, Object>> beanToArrayByField(Object object, Predicate<Field> fieldFilter) {
        List<Pair<String, Object>> list = Lists.newLinkedList();
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!fieldFilter.test(field)) {
                    continue;
                }
                String fieldName = field.getName();
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }
                list.add(Pair.of(fieldName, value));
            }
        }
        return list;
    }

    /**
     * 对象转map
     * key: 属性名
     * value: 属性值
     */
    public static Map<String, Object> beanToMapByField(Object object, Predicate<Field> fieldFilter) {
        List<Pair<String, Object>> list = beanToArrayByField(object, fieldFilter);
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(list.size());
        for (Pair<String, Object> p : list) {
            map.put(p.getLeft(), p.getRight());
        }
        return map;
    }

    private static Map<String, Object> bean2MapByGetter(Object javaBean) {
        Map<String, Object> map = new HashMap<>();
        Method[] methods = javaBean.getClass().getDeclaredMethods();
        try {
            for (Method method : methods) {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object value = method.invoke(javaBean, (Object[]) null);
                    map.put(field, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 递归处理,多层bean汇成一个map
     */
    private static Map<String, Object> beanToMap(Object o, String prefix) {
        if (o == null) {
            return emptyMap();
        }
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> map = PropertyUtils.describe(o);
            prefix = StringUtils.isNotEmpty(prefix) ? STR."\{prefix}." : "";
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (!o.equals(value)) {
                    result.putAll(beanToMap(value, prefix + key));
                    break;
                }
                result.put(prefix + key, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}