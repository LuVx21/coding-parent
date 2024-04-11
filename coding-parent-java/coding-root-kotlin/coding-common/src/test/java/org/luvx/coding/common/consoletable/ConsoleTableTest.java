package org.luvx.coding.common.consoletable;

import org.junit.Test;
import org.luvx.coding.common.consoletable.enums.Align;
import org.luvx.coding.common.consoletable.table.Cell;

import java.util.List;

import static java.util.List.of;

public class ConsoleTableTest {

    @Test
    public void m1() {
        List<Cell> header = of(Cell.of("name"), Cell.of("email"), Cell.of("tel"));
        List<List<Cell>> body = of(
                of(Cell.of("aaa"), Cell.of(Align.CENTER, "kat@gimal.com"), Cell.of(Align.RIGHT, "54321")),
                of(Cell.of("bbb"), Cell.of("ashe_111@hotmail.com"), Cell.of("9876543210")),
                of(Cell.of("ccc"), Cell.of("null"), Cell.of(Align.LEFT, "11"))
        );
        ConsoleTable.builder()
                .restrict(true)
                .addHeaders(header)
                .addRows(body)
                // .lineSep("\n")
                //  默认为: | - +
                // .verticalSep(":").horizontalSep("·").joinSep("*")
                // .nullPolicy(NullPolicy.NULL_STRING)
                .build()
                .render();
    }
}