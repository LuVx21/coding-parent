package org.luvx.coding.common.consoletable.table;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.luvx.coding.common.consoletable.util.PrintUtil;

import java.util.Collections;
import java.util.List;

public class Body {
    public List<List<Cell>> rows;

    public Body() {
        rows = Lists.newArrayList();
    }

    @SafeVarargs
    public final void addRows(List<Cell>... row) {
        Collections.addAll(rows, row);
    }

    public void addRows(List<List<Cell>> rowList) {
        rows.addAll(rowList);
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(rows);
    }

    /**
     * <blockquote><pre>
     * +------------+--------------+------------+
     * | one        | two          | three      |
     * +------------+--------------+------------+
     * </pre></blockquote>
     *
     * @param columnWidths  每列最大宽度
     * @param horizontalSep 行分隔符,默认'-'
     * @param verticalSep   列分隔符,默认'|'
     * @param joinSep       角连接符,默认'+'
     */
    public List<String> print(int[] columnWidths, String horizontalSep, String verticalSep, String joinSep) {
        List<String> result = Lists.newArrayList();
        if (!isEmpty()) {
            // 上部边框
            result.add(PrintUtil.printLineSep(columnWidths, horizontalSep, verticalSep, joinSep));
            // 行内容
            result.addAll(PrintUtil.printRows(rows, columnWidths, verticalSep));
            // 下部边框
            result.add(PrintUtil.printLineSep(columnWidths, horizontalSep, verticalSep, joinSep));
        }
        return result;
    }
}
