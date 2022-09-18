package org.luvx.common.cursor;

import java.util.List;

import javax.annotation.Nullable;

/**
 * 可用于按游标获取流式数据
 */
public interface CursorIterable<Id, Record> {
    /**
     * 从cursor开始（包括），读取limit条数据
     *
     * @param cursor 起始游标 >=cursor
     * @param limit 返回记录条数
     */
    List<Record> getByCursor(@Nullable Id cursor, int limit);
}
