package org.luvx.boot.common.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
public class DiffUtils {
    /**
     * newObj 相对于 oldObj 哪些属性发生了变化
     * 浅比较!
     *
     * @param newObj  新对象
     * @param oldObj  旧对象
     * @param include 包含的属性
     * @param exclude 忽略的属性
     * @return 字段
     */
    public static <T> List<String> diffValueField(T oldObj, T newObj,
                                                  Collection<String> include, Collection<String> exclude
    ) {
        if (!ObjectUtils.allNotNull(oldObj, newObj) || !oldObj.getClass().equals(newObj.getClass())) {
            return emptyList();
        }
        List<Triple<String, Object, ? extends Class<?>>> newValueList =
                getObjectPropertyValue(newObj, include, exclude);
        if (isEmpty(newValueList)) {
            return emptyList();
        }
        List<Triple<String, Object, ? extends Class<?>>> oldValueList =
                getObjectPropertyValue(oldObj, include, exclude);
        Map<String, Object> oldValueMap = Maps.newHashMap();
        for (Triple<String, Object, ? extends Class<?>> info : oldValueList) {
            oldValueMap.put(info.getLeft(), info.getMiddle());
        }

        return newValueList.stream()
                .filter(info -> oldValueMap.containsKey(info.getLeft()))
                .map(info -> {
                    String propertyName = info.getLeft();
                    Object newValue = info.getMiddle(), oldValue = oldValueMap.get(propertyName);
                    return newValue != null && !newValue.equals(oldValue) ? propertyName : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 通过反射获取对象的属性名称、getter返回值类型、属性值等信息
     *
     * @return 暂时使用元组, 使用较多时可优化为对象
     */
    public static List<Triple<String, Object, ? extends Class<?>>> getObjectPropertyValue(
            Object obj, Collection<String> include, Collection<String> exclude
    ) {
        if (obj == null) {
            return emptyList();
        }
        Class<?> objClass = obj.getClass();
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(objClass);
        if (ArrayUtils.isEmpty(descriptors)) {
            return emptyList();
        }
        Set<String> includeSet = isEmpty(include) ? Collections.emptySet() : Sets.newHashSet(include);
        Set<String> excludeSet = isEmpty(exclude) ? Collections.emptySet() : Sets.newHashSet(exclude);

        return Arrays.stream(descriptors)
                .filter(d -> d.getReadMethod() != null)
                .filter(d -> !excludeSet.contains(d.getName()))
                .filter(d -> includeSet.contains(d.getName()))
                .map(d -> {
                    Method readMethod = d.getReadMethod();
                    String name = d.getName();
                    Class<?> returnType = readMethod.getReturnType();
                    Object value;
                    try {
                        value = readMethod.invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(STR."反射获取类【\{objClass.getName()}】方法异常，", e);
                        return null;
                    }
                    return Triple.of(name, value, returnType);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
