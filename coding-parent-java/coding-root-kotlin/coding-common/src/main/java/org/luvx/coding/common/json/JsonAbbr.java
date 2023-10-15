package org.luvx.coding.common.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

/**
 * 在某个特定应用场景中，我们有一个从JSON获取的内容，比如：
 * m = { "a": 1, "b": { "c": 2, "d": [3,4] } }
 * 现在需要把这个层级的结构做展开，只保留一层key/value结构。对于上述输入，需要得到的结构是：
 * o = {"a": 1, "b.c": 2, "b.d": [3,4] }
 * 也就是说，原来需要通过 m["b"]["c"] 访问的值，在展开后可以通过 o["b.c"] 访问。
 * 请实现这个“层级结构展开”的代码。
 * 输入：任意JSON（或者map/dict）
 * 输出：展开后的JSON（或者map/dict）
 */
@Slf4j
public class JsonAbbr {
    /**
     * 循环: N 叉树 层次遍历
     */
    public static Map<String, Object> jsonAbbr(String json) {
        if (StringUtils.isBlank(json)) {
            return Maps.newHashMap();
        }
        json = json.strip();
        Queue<Entry<String, Object>> queue = Lists.newLinkedList();
        if (json.startsWith("{")) {
            JSONObject map = JSON.parseObject(json);
            queue.addAll(map.entrySet());
        } else if (json.startsWith("[")) {
            JSONArray array = JSON.parseArray(json);
            for (int i = 0; i < array.size(); i++) {
                queue.offer(new SimpleEntry<>(i + "", array.get(i)));
            }
        }

        Map<String, Object> result = Maps.newHashMap();
        while (!queue.isEmpty()) {
            Entry<String, Object> entry = queue.poll();
            Object value = entry.getValue();
            if (value instanceof Map) {
                ((Map<String, Object>) value).forEach((k, v) -> {
                    if (!(v instanceof Map) && !(v instanceof List)) {
                        result.put(entry.getKey() + "." + k, v);
                    }
                    queue.offer(new SimpleEntry<>(entry.getKey() + "." + k, v));
                });
            } else if (value instanceof List) {
                List array = (List) value;
                if (array.isEmpty()) {
                    continue;
                }
                for (int i = 0; i < array.size(); i++) {
                    String s = entry.getKey() + "." + i;
                    Object e = array.get(i);
                    if (e instanceof Map) {
                        Map<String, Object> mm = (Map<String, Object>) e;
                        for (Entry<String, Object> _entry : mm.entrySet()) {
                            queue.offer(new SimpleEntry<>(s + "." + _entry.getKey(), _entry.getValue()));
                        }
                    } else if (e instanceof List<?>) {
                        List ee = (List) e;
                        for (int k = 0; k < ee.size(); k++) {
                            Object o = ee.get(k);
                            queue.offer(new SimpleEntry<>(s + "." + k, o));
                        }
                    } else {
                        result.put(s, e);
                    }
                }
            } else {
                result.put(entry.getKey(), value);
            }
        }

        return result;
    }
}
