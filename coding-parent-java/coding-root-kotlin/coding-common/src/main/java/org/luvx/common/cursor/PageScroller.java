package org.luvx.common.cursor;

import static java.util.Collections.emptyList;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;

import javax.annotation.Nonnull;

import com.google.common.collect.AbstractIterator;

import lombok.Setter;

class PageScroller<Id, Record> implements Iterable<List<Record>> {
    static final boolean MODE_TRIM_FIRST = true;
    static final boolean MODE_TRIM_LAST  = false;

    private final CursorIterable<Id, Record> dao;
    private final Id                         initCursor;
    private final IntSupplier                bufferSize;
    private final Function<Record, Id>       entityIdFunction;
    private final boolean                    mode;

    @Setter
    private int maxNumberOfPages = Integer.MAX_VALUE;

    PageScroller(CursorIterable<Id, Record> dao, Id initCursor, IntSupplier bufferSize,
            Function<Record, Id> entityIdFunction, boolean mode) {
        this.dao = dao;
        this.initCursor = initCursor;
        this.bufferSize = bufferSize;
        this.entityIdFunction = entityIdFunction;
        this.mode = mode;
    }

    @Nonnull
    @Override
    public Iterator<List<Record>> iterator() {
        return mode == MODE_TRIM_FIRST ? new TrimFirstIterator() : new TrimLastIterator();
    }

    private class TrimFirstIterator extends AbstractIterator<List<Record>> {
        private List<Record> previousPage;
        private boolean      firstTime = true;
        private int          pageIndex = 0;

        @Override
        protected List<Record> computeNext() {
            int thisBufferSize = bufferSize.getAsInt();
            List<Record> page;
            if (firstTime) {
                firstTime = false;
                // 第一次, 正常取
                page = dao.getByCursor(initCursor, thisBufferSize);
            } else {
                if (pageIndex >= maxNumberOfPages) {
                    // 已经取到限制的页数了
                    page = emptyList();
                } else if (previousPage.size() < thisBufferSize) {
                    // 上页还不满, fail fast
                    page = emptyList();
                } else {
                    Id start = entityIdFunction.apply(previousPage.get(previousPage.size() - 1));
                    page = fetchOnePageExcludeStart(dao, start, thisBufferSize);
                }
            }

            previousPage = page;
            pageIndex++;
            return page.isEmpty() ? endOfData() : page;
        }

        /**
         * 由于 dao 实现中, start 是被包含的, 使用上一次 cursor 取的时候希望去除 start, 所以还需要多取一个
         */
        private List<Record> fetchOnePageExcludeStart(CursorIterable<Id, Record> dao, Id start, int limit) {
            List<Record> entities = dao.getByCursor(start, limit + 1);
            return entities.isEmpty() ? entities : entities.subList(1, entities.size());
        }
    }

    private class TrimLastIterator extends AbstractIterator<List<Record>> {

        private int     pageIndex = 0;
        private Id      cursor    = initCursor;
        private boolean noNext    = false;

        @Override
        protected List<Record> computeNext() {
            if (noNext) {
                return endOfData();
            }
            pageIndex++;
            if (pageIndex > maxNumberOfPages) {
                return endOfData();
            }
            int thisBufferSize = bufferSize.getAsInt();
            List<Record> list = dao.getByCursor(cursor, thisBufferSize + 1);
            if (list.isEmpty()) {
                return endOfData();
            }
            if (list.size() >= thisBufferSize + 1) {
                cursor = entityIdFunction.apply(list.get(thisBufferSize));
                return list.subList(0, thisBufferSize);
            } else {
                noNext = true;
                return list;
            }
        }
    }
}
