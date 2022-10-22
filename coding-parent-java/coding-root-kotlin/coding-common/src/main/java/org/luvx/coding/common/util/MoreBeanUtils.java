// package org.luvx.common.util;
//
// import static java.util.Collections.emptyList;
// import static org.apache.commons.collections4.CollectionUtils.isEmpty;
//
// import java.beans.PropertyDescriptor;
// import java.lang.reflect.InvocationTargetException;
// import java.lang.reflect.Method;
// import java.util.Arrays;
// import java.util.Collection;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.Set;
// import java.util.stream.Collectors;
//
// import org.apache.commons.beanutils.PropertyUtils;
// import org.apache.commons.lang3.ArrayUtils;
// import org.apache.commons.lang3.ObjectUtils;
// import org.apache.commons.lang3.tuple.Triple;
//
// import com.google.common.collect.Maps;
// import com.google.common.collect.Sets;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// public class MoreBeanUtils {
//
//     /**
//      * newObj 相对于 oldObj 哪些属性发生了变化
//      * 浅比较!
//      *
//      * @param newObj 新对象
//      * @param oldObj 旧对象
//      * @param include 包含的属性
//      * @param exclude 忽略的属性
//      * @return 字段
//      */
//     public static <T> List<String> diffValueField(T oldObj, T newObj,
//             Collection<String> include, Collection<String> exclude
//     ) {
//         if (!ObjectUtils.allNotNull(oldObj, newObj) || !oldObj.getClass().equals(newObj.getClass())) {
//             return emptyList();
//         }
//         List<Triple<String, Object, ? extends Class<?>>> newValueList =
//                 getObjectPropertyValue(newObj, include, exclude);
//         if (isEmpty(newValueList)) {
//             return emptyList();
//         }
//         List<Triple<String, Object, ? extends Class<?>>> oldValueList =
//                 getObjectPropertyValue(oldObj, include, exclude);
//         Map<String, Object> oldValueMap = Maps.newHashMap();
//         for (Triple<String, Object, ? extends Class<?>> info : oldValueList) {
//             oldValueMap.put(info.getLeft(), info.getMiddle());
//         }
//
//         return newValueList.stream()
//                 .filter(info -> oldValueMap.containsKey(info.getLeft()))
//                 .map(info -> {
//                     String propertyName = info.getLeft();
//                     Object newValue = info.getMiddle(), oldValue = oldValueMap.get(propertyName);
//                     return newValue != null && !newValue.equals(oldValue) ? propertyName : null;
//                 })
//                 .filter(Objects::nonNull)
//                 .collect(Collectors.toList());
//     }
//
//     /**
//      * 通过反射获取对象的属性名称、getter返回值类型、属性值等信息
//      *
//      * @return 暂时使用元组, 使用较多时可优化为对象
//      */
//     public static List<Triple<String, Object, ? extends Class<?>>> getObjectPropertyValue(
//             Object obj,
//             Collection<String> include, Collection<String> exclude
//     ) {
//         if (obj == null) {
//             return emptyList();
//         }
//         Class<?> objClass = obj.getClass();
//         PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(objClass);
//         if (ArrayUtils.isEmpty(descriptors)) {
//             return emptyList();
//         }
//         Set<String> includeSet = isEmpty(include) ? Collections.emptySet() : Sets.newHashSet(include);
//         Set<String> excludeSet = isEmpty(exclude) ? Collections.emptySet() : Sets.newHashSet(exclude);
//
//         return Arrays.stream(descriptors)
//                 .filter(d -> d.getReadMethod() != null)
//                 .filter(d -> !excludeSet.contains(d.getName()))
//                 .filter(d -> includeSet.contains(d.getName()))
//                 .map(d -> {
//                     Method readMethod = d.getReadMethod();
//                     String name = d.getName();
//                     Class<?> returnType = readMethod.getReturnType();
//                     Object value;
//                     try {
//                         value = readMethod.invoke(obj);
//                     } catch (IllegalAccessException | InvocationTargetException e) {
//                         log.error("反射获取类【" + objClass.getName() + "】方法异常，", e);
//                         return null;
//                     }
//                     return Triple.of(name, value, returnType);
//                 })
//                 .filter(Objects::nonNull)
//                 .collect(Collectors.toList());
//     }
//
//     /**
//      * 通过字段名获取方法数组
//      *
//      * @param beanClass Class<?>
//      * @param fieldNameArray 要输出的所有字段名数组
//      * @return Method[]
//      */
//     public static Method[] getMethods(Class<?> beanClass, String[] fieldNameArray) {
//         Method[] methodArray = new Method[fieldNameArray.length];
//
//         String methodName;
//         String fieldName;
//         for (int i = 0; i < fieldNameArray.length; i++) {
//             Method method = null;
//             fieldName = fieldNameArray[i];
//             methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//             try {
//                 method = beanClass.getMethod("get" + methodName, null);
//             } catch (SecurityException e) {
//                 e.printStackTrace();
//             } catch (NoSuchMethodException e) {
//                 try {
//                     method = beanClass.getMethod("is" + methodName, null);
//                 } catch (SecurityException e1) {
//                     e1.printStackTrace();
//                 } catch (NoSuchMethodException e1) {
//                     e1.printStackTrace();
//                 }
//             }
//             methodArray[i] = method;
//         }
//
//         return methodArray;
//     }
//
//     private static <K, V> Map<K, V> bean2Map(Object javaBean) {
//         Map<K, V> ret = new HashMap<>();
//         try {
//             Method[] methods = javaBean.getClass().getDeclaredMethods();
//             for (Method method : methods) {
//                 if (method.getName().startsWith("get")) {
//                     String field = method.getName();
//                     field = field.substring(field.indexOf("get") + 3);
//                     field = field.toLowerCase().charAt(0) + field.substring(1);
//                     Object value = method.invoke(javaBean, (Object[]) null);
//                     ret.put((K) field, (V) (null == value ? "" : value));
//                 }
//             }
//         } catch (Exception e) {
//         }
//         return ret;
//     }
//
//     private static Map objectToMap(Object o, String prefix) {
//         Map ret = new HashMap();
//         if (o == null) {
//             return ret;
//         }
//         try {
//             Map objDesc = PropertyUtils.describe(o);
//
//             prefix = (!("".equals(prefix))) ? prefix + "." : "";
//             for (Iterator it = objDesc.keySet().iterator(); it.hasNext(); ) {
//                 String key = it.next().toString();
//                 Object val = objDesc.get(key);
//                 if ((val != null) && (val instanceof CrmValueObject) && (!(o.equals(val)))) {
//                     ret.putAll(objectToMap(val, prefix + key));
//                     break;
//                 }
//                 ret.put(prefix + key, val);
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//             //logger.error(e);
//         }
//         //logger.debug("Object " + o + " convert to map: " + ret);
//         return ret;
//     }
//
//     public static Map objectToMap(List fieldNameList, Object object) {
//         Map ret = new HashMap<>();
//         for (Iterator it = fieldNameList.iterator(); it.hasNext(); ) {
//             String fieldName = (String) it.next();
//             String[] fs = fieldName.split(quote("."));
//             try {
//                 Object o = object;
//                 for (int i = 0; i < fs.length; ++i) {
//                     Map objDesc = PropertyUtils.describe(o);
//                     o = objDesc.get(fs[i]);
//                     if (o == null) {
//                         break;
//                     }
//                 }
//                 ret.put(fieldName, o);
//             } catch (Exception e) {
//                 e.printStackTrace();
//                 //logger.error(e);
//             }
//         }
//         return ret;
//     }
//
//     public static String quote(String s) {
//         int slashEIndex = s.indexOf("\\E");
//         if (slashEIndex == -1) {
//             return "\\Q" + s + "\\E";
//         }
//
//         StringBuffer sb = new StringBuffer(s.length() * 2);
//         sb.append("\\Q");
//         slashEIndex = 0;
//         int current = 0;
//         while ((slashEIndex = s.indexOf("\\E", current)) != -1) {
//             sb.append(s.substring(current, slashEIndex));
//             current = slashEIndex + 2;
//             sb.append("\\E\\\\E\\Q");
//         }
//         sb.append(s.substring(current, s.length()));
//         sb.append("\\E");
//         return sb.toString();
//     }
// }