package org.luvx.coding.common.annotation;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public class Annotations {
    /**
     * 获取方法上注解的值
     */
    @Immutable
    public static <T extends Annotation> Map<String, Object> annoValues(Method method, Class<T> annotationClass, String... methodNames) throws Exception {
        if (ObjectUtils.anyNull(method, annotationClass) || ArrayUtils.isEmpty(methodNames)) {
            return Collections.emptyMap();
        }
        T annotation = method.getAnnotation(annotationClass);

        Map<String, Object> result = Maps.newHashMapWithExpectedSize(methodNames.length);
        for (String methodName : methodNames) {
            Method prefix = annotationClass.getDeclaredMethod(methodName);
            Object value = prefix.invoke(annotation);
            result.put(methodName, value);
        }
        return result;
    }
}
