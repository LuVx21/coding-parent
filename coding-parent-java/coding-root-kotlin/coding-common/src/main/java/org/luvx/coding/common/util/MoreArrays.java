package org.luvx.coding.common.util;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;

import java.util.Arrays;
import java.util.Map;

public class MoreArrays {
    public static long[] newAndFill(int size, Int2LongFunction f) {
        long[] array = new long[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = f.apply(i);
        }
        return array;
    }

    /**
     * 从小到大排序后
     * [10,3,8,9,4] -> [1, 4, 2, 3, 0]
     *
     * @return 有序数组, 存储的不是元素本身, 而是元素在原数组中的位置
     */
    public static int[] argsort(long[] array) {
        final int len = array.length;
        Map<Long, Integer> map = Maps.newHashMapWithExpectedSize(len);
        for (int i = 0; i < len; i++) {
            map.putIfAbsent(array[i], i);
        }

        long[] na = array.clone();
        Arrays.sort(na);
        int[] result = new int[len];
        for (int i = 0; i < na.length; i++) {
            result[i] = map.get(na[i]);
        }
        return result;
    }
}
