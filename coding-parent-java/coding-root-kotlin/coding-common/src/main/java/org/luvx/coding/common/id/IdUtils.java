package org.luvx.coding.common.id;

import jakarta.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据业务 id 和业务类型生成一个新的 id
 * <pre>
 *     业务 id: 81_1992_0926 -> 111100011111111000010100100011110(33位)
 *     类型: 5 -> 101
 *     1. 类型左移 52 位: 1010000000000000000000000000000000000000000000000000000(55位)
 *     2. 业务 id 或操作: 1010000000000000000000111100011111111000010100100011110
 * </pre>
 */
public class IdUtils {
    /**
     * 最大值：0Xf_FFFF_FFFF_FFFFL
     * 业务 id 最大位数 52 位
     */
    private static final int  ID_BITS = 52;
    /**
     * 二进制: 52个1
     */
    private static final long ID_MASK = (1L << ID_BITS) - 1;

    public static long genCountId(@Nonnull ComboId id) {
        return (id.getType() << ID_BITS) | id.getSourceId();
    }

    public static ComboId parseComboId(long countId) {
        long comboId = countId & ID_MASK;
        long type = (countId >> ID_BITS);
        return new ComboId(comboId, type);
    }

    public static boolean isValidSourceId(long sourceId) {
        return sourceId <= ID_MASK;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ComboId {
        /**
         * 业务 id
         */
        private long sourceId;
        /**
         * 业务类型, 可定义为枚举
         */
        private long type;
    }
}