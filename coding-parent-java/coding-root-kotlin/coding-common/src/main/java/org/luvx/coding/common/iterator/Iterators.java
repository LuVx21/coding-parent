package org.luvx.coding.common.iterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiConsumer;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class Iterators {
    private Iterators() {
    }

    /**
     * 需要索引的遍历消费
     */
    public static <T> void forEach(Collection<T> list, BiConsumer<Integer, T> consumer) {
        if (isEmpty(list)) {
            return;
        }
        Iterator<T> iterator = list.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            consumer.accept(i, iterator.next());
        }
    }

    /**
     * 两个集合对应位置元素有关联
     */
    public static <T1, T2> void forEach(Collection<T1> list1, Collection<T2> list2, BiConsumer<T1, T2> consumer) {
        if (isEmpty(list1) || isEmpty(list2)) {
            return;
        }
        Iterator<T1> iterator1 = list1.iterator();
        Iterator<T2> iterator2 = list2.iterator();
        for (int i = 0; iterator1.hasNext() && iterator2.hasNext(); i++) {
            consumer.accept(iterator1.next(), iterator2.next());
        }
    }
}
