package org.luvx.coding.common.consoletable.enums;

import org.luvx.coding.common.consoletable.table.Cell;

public enum NullPolicy {
    THROW {
        @Override
        public Cell getCell(Cell cell) {
            throw new IllegalArgumentException("cell or value is null: " + cell);
        }
    },
    NULL_STRING {
        @Override
        public Cell getCell(Cell cell) {
            if (cell == null) {
                return Cell.of("null");
            }
            cell.setValue("null");
            return cell;
        }
    },
    EMPTY_STRING {
        @Override
        public Cell getCell(Cell cell) {
            if (cell == null) {
                return Cell.EMPTY;
            }
            cell.setValue("");
            return cell;
        }
    };

    public abstract Cell getCell(Cell cell);
}
