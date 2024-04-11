package org.luvx.coding.common.consoletable.table;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.luvx.coding.common.consoletable.util.PrintUtil;

import java.util.Collections;
import java.util.List;

public class Header {
    public List<Cell> row;

    public Header() {
        this.row = Lists.newArrayList();
    }

    public void addHeaders(Cell... cs) {
        Collections.addAll(row, cs);
    }

    public void addHeaders(List<Cell> headers) {
        row.addAll(headers);
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(row);
    }

    /**
     * <blockquote><pre>
     * +------------+--------------+------------+
     * | one        | two          | three      |
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
            // 顶部边框
            result.add(PrintUtil.printLineSep(columnWidths, horizontalSep, verticalSep, joinSep));
            // header部
            result.addAll(PrintUtil.printRows(Collections.singletonList(row), columnWidths, verticalSep));
        }
        return result;
    }
}
