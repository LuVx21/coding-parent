package org.luvx.coding.common.consoletable;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.luvx.coding.common.consoletable.enums.NullPolicy;
import org.luvx.coding.common.consoletable.table.Body;
import org.luvx.coding.common.consoletable.table.Cell;
import org.luvx.coding.common.consoletable.table.Header;
import org.luvx.coding.common.consoletable.util.StringPadUtil;

import java.util.IntSummaryStatistics;
import java.util.List;

@Slf4j
public class ConsoleTable {

    private String lineSep     = "\n";
    private String verticalSep = "|", horizontalSep = "-", joinSep = "+";

    private Header     header;
    private Body       body;
    private int[]      columnWidths;
    private NullPolicy nullPolicy = NullPolicy.EMPTY_STRING;
    private boolean    restrict   = false;

    private ConsoleTable() {
    }

    String getContent() {
        return StringUtils.join(getLines(), lineSep);
    }

    List<String> getLines() {
        List<String> lines = Lists.newArrayList();
        if (header != null && !header.isEmpty()) {
            lines.addAll(header.print(columnWidths, horizontalSep, verticalSep, joinSep));
        }
        if (body != null && !body.isEmpty()) {
            lines.addAll(body.print(columnWidths, horizontalSep, verticalSep, joinSep));
        }
        return lines;
    }

    void render() {
        for (String line : getLines()) {
            System.out.println(line);
        }
    }

    void renderLog() {
        for (String line : getLines()) {
            log.info(line);
        }
    }

    @Override
    public String toString() {
        return getContent();
    }

    public static Builder builder() {
        return new Builder(new ConsoleTable());
    }

    public static class Builder {
        ConsoleTable consoleTable;

        private Builder(ConsoleTable ct) {
            consoleTable = ct;
            consoleTable.header = new Header();
            consoleTable.body = new Body();
        }

        public Builder addHeaders(Cell... cells) {
            consoleTable.header.addHeaders(cells);
            return this;
        }

        public Builder addHeaders(List<Cell> headers) {
            consoleTable.header.addHeaders(headers);
            return this;
        }

        @SafeVarargs
        public final Builder addRows(List<Cell>... rows) {
            consoleTable.body.addRows(rows);
            return this;
        }

        public Builder addRows(List<List<Cell>> rows) {
            consoleTable.body.addRows(rows);
            return this;
        }

        public Builder lineSep(String lineSep) {
            consoleTable.lineSep = lineSep;
            return this;
        }

        public Builder verticalSep(String verticalSep) {
            consoleTable.verticalSep = verticalSep;
            return this;
        }

        public Builder horizontalSep(String horizontalSep) {
            consoleTable.horizontalSep = horizontalSep;
            return this;
        }

        public Builder joinSep(String joinSep) {
            consoleTable.joinSep = joinSep;
            return this;
        }

        public Builder nullPolicy(NullPolicy nullPolicy) {
            consoleTable.nullPolicy = nullPolicy;
            return this;
        }

        public Builder restrict(boolean restrict) {
            consoleTable.restrict = restrict;
            return this;
        }

        public ConsoleTable build() {
            if (consoleTable.header.isEmpty() && consoleTable.body.isEmpty()) {
                return consoleTable;
            }

            List<List<Cell>> allRows = Lists.newArrayList();
            allRows.add(consoleTable.header.row);
            allRows.addAll(consoleTable.body.rows);

            IntSummaryStatistics iis = allRows.stream().map(List::size).mapToInt(s -> s).summaryStatistics();
            int maxColumn = iis.getMax(), minColumn = iis.getMin();

            if (consoleTable.restrict && maxColumn != minColumn) {
                throw new IllegalArgumentException("number of columns for each row must be the same when strict mode used.");
            }
            consoleTable.columnWidths = new int[maxColumn];
            for (List<Cell> row : allRows) {
                for (int i = 0; i < row.size(); i++) {
                    Cell cell = row.get(i);
                    if (cell == null || cell.getValue() == null) {
                        cell = consoleTable.nullPolicy.getCell(cell);
                        row.set(i, cell);
                    }
                    int length = StringPadUtil.strLength(cell.getValue());
                    if (consoleTable.columnWidths[i] < length) {
                        consoleTable.columnWidths[i] = length;
                    }
                }
            }
            return consoleTable;
        }
    }
}
