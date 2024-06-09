package org.luvx.coding.common.ds;

import org.junit.jupiter.api.Test;

class RingTest {

    private void print(Ring<?> head) {
        System.out.print(head.getData());
        for (var c = head.next(); c != head; c = c.next()) {
            System.out.print("->" + c.getData());
        }
        System.out.println();
    }

    @Test
    void m1() {
        Ring<Integer> head = Ring.ofDataList(1, 2, 3, 4, 5);
        Ring<Integer> head1 = Ring.ofDataList(101, 102, 103, 104, 105);

        // head.link(head1);
        // print(head);

        Ring<Integer> move = head.move(2);
        print(move);

        print(head);
        Ring<Integer> link = head.link(move);
        print(head);
        print(link);
    }
}