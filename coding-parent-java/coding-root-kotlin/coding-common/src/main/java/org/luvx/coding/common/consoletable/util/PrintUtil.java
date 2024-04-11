package org.luvx.coding.common.consoletable.util;

import com.google.common.collect.Lists;
import org.luvx.coding.common.consoletable.enums.Align;
import org.luvx.coding.common.consoletable.table.Cell;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class PrintUtil {

    /**
     * @param columnWidths  每列宽度
     * @param horizontalSep 行分隔符,默认'-'
     * @param verticalSep   列分隔符,默认'|'
     * @param joinSep       角连接符,默认'+'
     * @return like:
     * +------------+--------------+------------+
     */
    public static String printLineSep(int[] columnWidths, String horizontalSep, String verticalSep, String joinSep) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < columnWidths.length; i++) {
            String l = String.join("", Collections.nCopies(columnWidths[i] + StringPadUtil.strLength(verticalSep) + 1, horizontalSep));
            line.append(joinSep).append(l).append(i == columnWidths.length - 1 ? joinSep : "");
        }
        return line.toString();
    }

    /**
     * @param rows         数据行
     * @param columnWidths 每列宽度
     * @param verticalSep  列分隔符,默认'|'
     * @return like:
     * | super      | broccoli     | flexible   |
     * | assumption | announcement | reflection |
     * | logic      | pleasant     | wild       |
     */
    public static List<String> printRows(List<List<Cell>> rows, int[] columnWidths, String verticalSep) {
        List<String> result = Lists.newArrayList();
        for (List<Cell> row : rows) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.size(); i++) {
                Cell cell = defaultIfNull(row.get(i), Cell.EMPTY);
                String verStrTemp = i == row.size() - 1 ? verticalSep : "";
                String value = cell.getValue();
                String apply = switch (defaultIfNull(cell.getAlign(), Align.RIGHT)) {
                    case LEFT -> StringPadUtil.rightPad(value, columnWidths[i]);
                    case CENTER -> StringPadUtil.center(value, columnWidths[i]);
                    default -> StringPadUtil.leftPad(value, columnWidths[i]);
                };
                sb.append(STR."\{verticalSep} \{apply} \{verStrTemp}");
            }
            result.add(sb.toString());
        }
        return result;
    }
}
