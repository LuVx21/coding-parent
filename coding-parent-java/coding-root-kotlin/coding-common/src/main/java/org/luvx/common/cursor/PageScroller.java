package org.luvx.common.cursor;

import static java.util.Collections.emptyList;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;

import com.google.common.collect.AbstractIterator;

import lombok.Setter;

class PageScroller<Id, Record> implements Iterable<List<Record>> {
    static final boolean MODE_TRIM_FIRST = true;
    static final boolean MODE_TRIM_LAST  = false;

    private final CursorIterable<Id, Record> dataRetriever;
    private final Id                         initCursor;
    private final IntSupplier                limitSupplier;
    private final Function<Record, Id>       cursorExtractor;
    private final boolean                    mode;

    @Setter
    private int maxPage = Integer.MAX_VALUE;

    PageScroller(CursorIterable<Id, Record> dataRetriever, Id initCursor, IntSupplier limitSupplier,
            Function<Record, Id> cursorExtractor, boolean mode) {
        this.dataRetriever = dataRetriever;
        this.initCursor = initCursor;
        this.limitSupplier = limitSupplier;
        this.cursorExtractor = cursorExtractor;
        this.mode = mode;
    }

    @Override
    public Iterator<List<Record>> iterator() {
        return mode == MODE_TRIM_FIRST ? new TrimFirstIterator() : new TrimLastIterator();
    }

    /**
     * 以上次list的最后元素临时计算出当前 cursor
     * 取得 limit+1 个, 并移除第一个(当前cursor在上一次已经获取到了)
     */
    private class TrimFirstIterator extends AbstractIterator<List<Record>> {
        private List<Record> prePage;
        private boolean      firstTime = true;
        private int          pageIndex = 0;

        @Override
        protected List<Record> computeNext() {
            int limit = limitSupplier.getAsInt();
            List<Record> page;
            if (firstTime) {
                firstTime = false;
                page = dataRetriever.getByCursor(initCursor, limit);
            } else {
                if (pageIndex >= maxPage) {
                    page = emptyList();
                } else if (prePage.size() < limit) {
                    page = emptyList();
                } else {
                    Id cursor = cursorExtractor.apply(prePage.get(prePage.size() - 1));
                    List<Record> data = dataRetriever.getByCursor(cursor, limit + 1);
                    page = data.isEmpty() ? data : data.subList(1, data.size());
                }
            }

            prePage = page;
            pageIndex++;
            return page.isEmpty() ? endOfData() : page;
        }
    }

    /**
     * 以上次计算出的当前 cursor
     * 取得 limit+1 个, 以第limit+1个计算下次的起点, 并移除这个
     */
    private class TrimLastIterator extends AbstractIterator<List<Record>> {
        private Id      cursor    = initCursor;
        private boolean noNext    = false;
        private int     pageIndex = 0;

        @Override
        protected List<Record> computeNext() {
            if (noNext) {
                return endOfData();
            }
            pageIndex++;
            if (pageIndex > maxPage) {
                return endOfData();
            }
            int limit = limitSupplier.getAsInt();
            List<Record> list = dataRetriever.getByCursor(cursor, limit + 1);
            if (list.isEmpty()) {
                return endOfData();
            }
            // 如果出现>, 说明 cursor 可能不具有唯一性
            if (list.size() >= limit + 1) {
                cursor = cursorExtractor.apply(list.get(limit));
                return list.subList(0, limit);
            } else {
                noNext = true;
                return list;
            }
        }
    }
}
