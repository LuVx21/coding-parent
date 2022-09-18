package org.luvx.common.cursor;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * 可用于按游标获取流式数据
 */
@FunctionalInterface
public interface CursorIterable<Id, Record> {
    /**
     * 从cursor开始（包括），读取limit条数据
     *
     * @param cursor 起始游标 >=cursor
     * @param limit 返回记录条数
     */
    @Nonnull
    List<Record> getByCursor(@Nonnull Id cursor, int limit);
}
