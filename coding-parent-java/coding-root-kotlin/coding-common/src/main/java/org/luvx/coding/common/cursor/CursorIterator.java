package org.luvx.coding.common.cursor;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.luvx.coding.common.more.MoreStreams;

import com.google.common.collect.AbstractIterator;

/**
 * @param <Id> 具有唯一性的游标值
 * @param <Record> 实体对象
 */
public class CursorIterator<Id, Record> implements Iterable<Record> {
    private static final int DEFAULT_BUFFER_SIZE = 30;

    private final PageScroller<Id, Record> pageScroller;

    private CursorIterator(PageScroller<Id, Record> pageScroller) {
        this.pageScroller = pageScroller;
    }

    /**
     * 获取迭代器
     */
    @Override
    public Iterator<Record> iterator() {
        return new AbstractIterator<>() {
            private final Iterator<List<Record>> pageIterator = pageScroller.iterator();
            private Iterator<Record> entityIteratorInPage;

            @Override
            protected Record computeNext() {
                if (entityIteratorInPage == null || !entityIteratorInPage.hasNext()) {
                    if (!pageIterator.hasNext()) {
                        return endOfData();
                    }
                    entityIteratorInPage = pageIterator.next().iterator();
                }
                return entityIteratorInPage.next();
            }
        };
    }

    /**
     * 获取Stream
     *
     * @return 返回一个Stream对象
     */
    public Stream<Record> stream() {
        return MoreStreams.toStream(iterator());
    }

    @CheckReturnValue
    public static <Id, Entity> Builder<Id, Entity> builder() {
        return new Builder<>();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<Id, Entity> {
        private CursorIterable<Id, Entity> dataRetriever;
        private IntSupplier                limitSupplier;
        private Function<Entity, Id>       cursorExtractor;
        private Id                         initCursor;
        private int                        maxPage = 0;

        /**
         * 游标值提取方法
         */
        @CheckReturnValue
        public Builder<Id, Entity> withCursorExtractor(Function<Entity, Id> cursorExtractor) {
            requireNonNull(cursorExtractor);
            this.cursorExtractor = cursorExtractor;
            return this;
        }

        /**
         * 起始游标值, 对应的数据也是返回的第一条数据
         */
        @CheckReturnValue
        public Builder<Id, Entity> withInitCursor(Id initCursor) {
            this.initCursor = initCursor;
            return this;
        }

        @CheckReturnValue
        public Builder<Id, Entity> limit(int limit) {
            checkArgument(limit > 0);
            return limit(() -> limit);
        }

        @CheckReturnValue
        public Builder<Id, Entity> limit(@Nonnull IntSupplier limitSupplier) {
            this.limitSupplier = requireNonNull(limitSupplier);
            return this;
        }

        @CheckReturnValue
        public Builder<Id, Entity> maxPage(int maxPage) {
            this.maxPage = maxPage;
            return this;
        }

        @CheckReturnValue
        public Builder<Id, Entity> withDataRetriever(CursorIterable<Id, Entity> dataRetriever) {
            requireNonNull(dataRetriever);
            this.dataRetriever = dataRetriever;
            return this;
        }

        public CursorIterator<Id, Entity> build() {
            limitSupplier = Objects.requireNonNullElse(limitSupplier, () -> DEFAULT_BUFFER_SIZE);

            PageScroller<Id, Entity> scroller = new PageScroller<>(dataRetriever, initCursor, limitSupplier,
                    cursorExtractor, PageScroller.MODE_TRIM_LAST);
            if (maxPage > 0) {
                scroller.setMaxPage(maxPage);
            }
            return new CursorIterator<>(scroller);
        }
    }
}
