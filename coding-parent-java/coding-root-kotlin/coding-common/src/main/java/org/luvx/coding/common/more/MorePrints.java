package org.luvx.coding.common.more;

import io.vavr.collection.Iterator;
import org.luvx.coding.common.consoletable.ConsoleTable;
import org.luvx.coding.common.consoletable.table.Cell;
import org.luvx.coding.common.util.ToString;

import java.util.Arrays;
import java.util.List;

public class MorePrints {
    public static void printlnBit(int n) {
        System.out.printf("%32s\n", Integer.toBinaryString(n));
    }

    public static void println(Object... objs) {
        System.out.println(
                Iterator.of(objs)
                        .map(String::valueOf)
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
                .renderLog();
    }
}
