package org.luvx.coding.common.more;

import io.vavr.collection.Iterator;
import org.luvx.coding.common.consoletable.ConsoleTable;
import org.luvx.coding.common.consoletable.table.Cell;
import org.luvx.coding.common.util.ToString;

import java.util.Arrays;
import java.util.List;

public class MorePrints {
    public static void printlnBit(long n) {
        String format = String.format("%64s", Long.toBinaryString(n));
        char[] array = format.toCharArray();

        byte[] r = new byte[64 + 7];
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (i != 0 && i % 8 == 0) {
                r[j++] = '_';
            }
            r[j++] = (byte) array[i];
        }
        System.out.println(new String(r));
    }

    public static void println(Object... objs) {
        System.out.println(
                Iterator.of(objs)
                        .map(ToString::toString)
                        .mkString("\n")
        );
    }

    public static void printlnTable(Object... objs) {
        List<Cell> row = Arrays.stream(objs)
                .map(ToString::toString)
                .map(Cell::of)
                .toList();

        ConsoleTable.builder()
                .restrict(false)
                .addRows(row)
                .build()
                .render();
    }
}
