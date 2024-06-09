package org.luvx.coding.common.ds;

import lombok.Getter;
import lombok.Setter;

import jakarta.annotation.Nullable;
import java.util.function.Consumer;

public class Ring<T> {
    @Setter
    @Getter
    @Nullable
    private T       data;
    private Ring<T> pre;
    private Ring<T> next;

    private Ring(@Nullable T data) {
        this.data = data;
    }

    public Ring<T> pre() {
        if (pre == null) {
            pre = this;
        }
        return pre;
    }

    public Ring<T> next() {
        if (next == null) {
            next = this;
        }
        return next;
    }

    /**
     * 移动n个, 负数向前,正数向后
     */
    public Ring<T> move(int n) {
        if (next == null) {
            next = this;
        }
        var cur = this;
        if (n < 0) {
            for (int i = n; i < 0; i++) {
                cur = cur.pre;
            }
        } else if (n > 0) {
            for (int i = n; i > 0; i--) {
                cur = cur.next;
            }
        }
        return cur;
    }

    public Ring<T> link(Ring<T> s) {
        var n = next;
        if (s != null) {
            Ring<T> p = s.pre;
            next = s;
            s.pre = this;
            n.pre = p;
            p.next = n;
        }
        return n;
    }

    public Ring<T> unlink(int n) {
        if (n <= 0) {
            return null;
        }
        return link(move(n + 1));
    }

    public int size() {
        int size = 1;
        for (var c = next; c != this; c = c.next) {
            size++;
        }
        return size;
    }

    public void exec(Consumer<T> consumer) {
        consumer.accept(data);
        for (var c = next; c != this; c = c.next) {
            consumer.accept(c.data);
        }
    }

    public static <T> Ring<T> of(T data) {
        Ring<T> r = new Ring<>(data);
        r.pre = r;
        r.next = r;
        return r;
    }

    /**
     * 创建含有n个元素的ring
     */
    @Nullable
    public static <T> Ring<T> of(int n) {
        if (n <= 0) {
            return null;
        }
        Ring<T> head = new Ring<>(null), c = head;
        for (int i = 1; i < n; i++) {
            Ring<T> nn = new Ring<>(null);
            nn.pre = c;
            c.next = nn;
            c = c.next;
        }
        c.next = head;
        head.pre = c;
        return head;
    }

    public static <T> Ring<T> ofDataList(T... dataList) {
        Ring<T> head = new Ring<>(dataList[0]), c = head;
        for (int i = 1; i < dataList.length; i++) {
            Ring<T> n = new Ring<>(dataList[i]);
            n.pre = c;
            c.next = n;
            c = c.next;
        }
        c.next = head;
        head.pre = c;
        return head;
    }
}
