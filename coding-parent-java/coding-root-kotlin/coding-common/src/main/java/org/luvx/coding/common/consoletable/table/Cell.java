package org.luvx.coding.common.consoletable.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.luvx.coding.common.consoletable.enums.Align;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class Cell {
    public static final Cell EMPTY = Cell.of("");

    private Align  align;
    private String value;

    public static Cell of(Align align, String value) {
        return new Cell(align, value);
    }

    public static Cell of(String value) {
        return new Cell(Align.RIGHT, value);
    }
}
