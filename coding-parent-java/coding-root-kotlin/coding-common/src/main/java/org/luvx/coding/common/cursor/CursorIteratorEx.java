package org.luvx.coding.common.cursor;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 主要逻辑全在RollingIterator中
 * <pre>
 * {@code
 *     CursorIterator<Item, String, List<Item>> cursorIterator =
 *         CursorIterator.<Item, String, List<Item>>builder()
 *                 .withInitCursor(Optional.ofNullable(firstGreater.apply(null))
 *                         .map(VocBasicInfoPO::getId)
 *                         .orElse(null)
 *                 )
 *                 .withEndChecker(StringUtils::isEmpty)
 *                 .firstCursorCheckEnd(true)
 *                 .withDataAccessor((String cursor) -> dao.apply(cursor, limit))
 *                 .withDataExtractor(List::iterator)
 *                 .withCursorExtractor((List<Item> _list) -> {
 *                     if (CollectionUtils.size(_list) < limit) {
 *                         return null;
 *                     }
 *                     Item last = _list.get(_list.size() - 1);
 *                     VocBasicInfoPO apply = firstGreater.apply(last.getId());
 *                     return apply.getId();
 *                 })
 *                 .build();
 * }
 * </pre>
 *
 * @param <ITEM> 返回实体的类型
 * @param <ID> ID类型
 * @param <ITEMS> 列表读取结果对象类型
 */
public class CursorIteratorEx<ITEM, ID, ITEMS> implements Iterable<ITEM> {
    /* 初始游标值,从此值开始迭代 */
    private final ID                              initCursor;
    /* 是否检查初始游标值 */
    private final boolean                         checkFirstCursor;
    private final RateLimiter                     rateLimiter;
    /* 用于获取批次数据 */
    private final Function<ID, ITEMS>             dataAccessor;
    /* 处理dataAccessor的结果获取批次数据的游标, 其执行结果被用于endChecker */
    private final Function<ITEMS, ID>             cursorExtractor;
    /* 处理dataAccessor的结果获取迭代器 */
    private final Function<ITEMS, Iterator<ITEM>> dataExtractor;
    /* 游标终止检查 */
    private final Predicate<ID>                   endChecker;

    private CursorIteratorEx(ID initCursor, boolean checkFirstCursor,
            RateLimiter rateLimiter, Function<ID, ITEMS> dataAccessor,
            Function<ITEMS, ID> cursorExtractor, Function<ITEMS, Iterator<ITEM>> dataExtractor,
            Predicate<ID> endChecker) {
        this.initCursor = initCursor;
        this.checkFirstCursor = checkFirstCursor;
        this.rateLimiter = rateLimiter;
        this.dataAccessor = dataAccessor;
        this.cursorExtractor = cursorExtractor;
        this.dataExtractor = dataExtractor;
        this.endChecker = endChecker;
    }

    /**
     * 创建游标迭代器构造器对象
     *
     * @return 游标迭代器构造器对象
     */
    @Nonnull
    @CheckReturnValue
    public static <ITEM, ID, ITEMS> Builder<ITEM, ID, ITEMS> builder() {
        return new Builder<>();
    }

    /**
     * 获取迭代器
     *
     * @return 返回迭代器对象
     */
    @Nonnull
    @Override
    public Iterator<ITEM> iterator() {
        return new RollingIterator();
    }

    /**
     * 获取Stream
     *
     * @return 返回一个Stream对象
     */
    public Stream<ITEM> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(),
                (Spliterator.NONNULL | Spliterator.IMMUTABLE)), false);
    }

    /**
     * 游标迭代器构造器
     *
     * @param <ITEM> 返回实体的类型泛型
     * @param <ID> ID类型泛型
     * @param <ITEMS> 列表读取结果对象的泛型
     */
    @SuppressWarnings("unchecked")
    public static final class Builder<ITEM, ID, ITEMS> {
        private ID                              initCursor;
        private boolean                         checkFirstCursor;
        private RateLimiter                     rateLimiter;
        private Function<ID, ITEMS>             dataAccessor;
        private Function<ITEMS, ID>             cursorExtractor;
        private Function<ITEMS, Iterator<ITEM>> dataExtractor;
        private Predicate<ID>                   endChecker;

        /**
         * 设置起始ID，此ID对应的记录将作为迭代器返回的第一条数据对象
         *
         * @param initCursor 起始ID
         * @return 当前构造器对象
         */
        @CheckReturnValue
        @Nonnull
        public Builder<ITEM, ID, ITEMS> withInitCursor(ID initCursor) {
            this.initCursor = initCursor;
            return this;
        }

        /**
         * 设置是否对首个传入的游标进行终末检查
         *
         * @param check 是否对首个传入的游标进行终末检查，默认不检查
         * @return 当前构造器对象
         */
        @CheckReturnValue
        @Nonnull
        public Builder<ITEM, ID, ITEMS> firstCursorCheckEnd(boolean check) {
            this.checkFirstCursor = check;
            return this;
        }

        public Builder<ITEM, ID, ITEMS> withRateLimiter(@Nullable RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
            return this;
        }

        /**
         * 数据读取函数
         *
         * @param dataAccessor 数据读取函数，传入当前起始的ID，返回查询结果对象
         * @return 当前构造器对象
         */
        @CheckReturnValue
        @Nonnull
        public Builder<ITEM, ID, ITEMS> withDataAccessor(@Nonnull Function<ID, ITEMS> dataAccessor) {
            this.dataAccessor = dataAccessor;
            return this;
        }

        /**
         * 获取下一条游标函数
         *
         * @param cursorExtractor 获取下一条游标函数，传入当前获得的查询结果对象，返回下一条游标ID对象
         * @return 当前构造器对象
         */
        @CheckReturnValue
        @Nonnull
        public Builder<ITEM, ID, ITEMS> withCursorExtractor(@Nonnull Function<ITEMS, ID> cursorExtractor) {
            this.cursorExtractor = cursorExtractor;
            return this;
        }

        /**
         * 数据提取器函数
         *
         * @param dataExtractor 数据提取器函数，传入当前获得的查询结果对象，返回结果实体集合的迭代器对象
         * @return 当前构造器对象
         */
        @CheckReturnValue
        @Nonnull
        public Builder<ITEM, ID, ITEMS> withDataExtractor(@Nonnull Function<ITEMS, Iterator<ITEM>> dataExtractor) {
            this.dataExtractor = dataExtractor;
            return this;
        }

        /**
         * 设置游标终末检查器
         *
         * @param endChecker 游标终末检查器，传入当前的游标值，判断是否还有下一条记录存在，有返回true，否则返回false。
         * 默认根据游标是否为null来进行判断，不为空证明有下一条记录
         * @return 当前构造器对象
         */
        @CheckReturnValue
        @Nonnull
        public Builder<ITEM, ID, ITEMS> withEndChecker(Predicate<ID> endChecker) {
            this.endChecker = endChecker;
            return this;
        }

        /**
         * 构造游标迭代器
         *
         * @return 游标迭代器对象
         */
        @SuppressWarnings("rawtypes")
        @Nonnull
        public CursorIteratorEx<ITEM, ID, ITEMS> build() {
            ensure();
            return new CursorIteratorEx(initCursor, checkFirstCursor,
                    rateLimiter, dataAccessor,
                    cursorExtractor, dataExtractor, endChecker);
        }

        private void ensure() {
            Objects.requireNonNull(dataExtractor, "data extractor is null.");
            Objects.requireNonNull(dataAccessor, "data retriever is null.");
            Objects.requireNonNull(cursorExtractor, "cursor extractor is null.");
            endChecker = Objects.requireNonNullElse(endChecker, Objects::isNull);
        }
    }

    /**
     * 迭代的主逻辑
     */
    private final class RollingIterator implements Iterator<ITEM> {
        private ID             currentCursor;
        private ITEMS          currentData;
        private Iterator<ITEM> currentIterator;
        private RateLimiter    _rateLimiter;

        RollingIterator() {
            currentCursor = initCursor;
            _rateLimiter = rateLimiter;
            // 检查游标
            if (checkFirstCursor && endChecker.test(currentCursor)) {
                return;
            }
            // 读取数据
            currentData = dataAccessor.apply(currentCursor);
            if (currentData != null) {
                // 数据结果转为迭代器
                currentIterator = dataExtractor.apply(currentData);
                // 数据结果提取下次执行的游标
                currentCursor = cursorExtractor.apply(currentData);
            }
        }

        @Override
        public boolean hasNext() {
            if (currentIterator == null) {
                return false;
            }
            if (currentIterator.hasNext()) {
                return true;
            }
            // 上次拉取的数据已经迭代结束, 需再次拉取数据
            roll();
            return currentIterator != null && currentIterator.hasNext();
        }

        private void roll() {
            // 再次拉取前检查游标
            if (endChecker.test(currentCursor)) {
                currentData = null;
                currentIterator = null;
                return;
            }
            // 再次拉取数据
            if (_rateLimiter != null) {
                _rateLimiter.acquire();
            }
            currentData = dataAccessor.apply(currentCursor);
            if (currentData == null) {
                currentIterator = null;
            } else {
                currentIterator = dataExtractor.apply(currentData);
                currentCursor = cursorExtractor.apply(currentData);
            }
        }

        @Override
        public ITEM next() {
            return currentIterator.next();
        }
    }
}
