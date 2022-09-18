package org.luvx.common.cursor;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.luvx.common.cursor.PageScroller.MODE_TRIM_FIRST;
import static org.luvx.common.cursor.PageScroller.MODE_TRIM_LAST;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.luvx.common.more.MoreStreams;

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
    @Nonnull
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
        private IntSupplier          bufferSize;
        private Function<Entity, Id> extractor;
        private Id                   start;
        private int                  limit = 0;
        private boolean              mode  = MODE_TRIM_FIRST;

        /**
         * 游标值提取方法
         */
        @CheckReturnValue
        @Nonnull
        public Builder<Id, Entity> cursorExtractor(Function<Entity, Id> extractor) {
            this.extractor = extractor;
            return this;
        }

        /**
         * 起始游标值, 对应的数据也是返回的第一条数据
         */
        @CheckReturnValue
        @Nonnull
        public Builder<Id, Entity> start(Id start) {
            this.start = start;
            return this;
        }

        @CheckReturnValue
        @Nonnull
        public Builder<Id, Entity> bufferSize(int bufferSize) {
            checkArgument(bufferSize > 0);
            return bufferSize(() -> bufferSize);
        }

        @CheckReturnValue
        @Nonnull
        public Builder<Id, Entity> bufferSize(@Nonnull IntSupplier bufferSize) {
            this.bufferSize = requireNonNull(bufferSize);
            return this;
        }

        @CheckReturnValue
        @Nonnull
        public Builder<Id, Entity> limit(int limit) {
            this.limit = limit;
            return this;
        }

        @Nonnull
        public CursorIterator<Id, Entity> build(CursorIterable<Id, Entity> dataRetriever) {
            requireNonNull(dataRetriever);
            requireNonNull(extractor);
            bufferSize = Objects.requireNonNullElse(bufferSize, () -> DEFAULT_BUFFER_SIZE);

            this.mode = MODE_TRIM_LAST;

            PageScroller<Id, Entity> scroller = new PageScroller<>(dataRetriever, start, bufferSize, extractor, mode);
            if (limit > 0) {
                scroller.setMaxNumberOfPages(limit);
            }
            return new CursorIterator<>(scroller);
        }
    }
}
