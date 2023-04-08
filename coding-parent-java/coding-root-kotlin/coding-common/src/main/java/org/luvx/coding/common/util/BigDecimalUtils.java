package org.luvx.coding.common.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.ArrayUtils;

public class BigDecimalUtils {
    public static BigDecimal sum(BigDecimal... nums) {
        BigDecimal result = BigDecimal.ZERO;
        if (ArrayUtils.isEmpty(nums)) {
            return result;
        }
        for (BigDecimal num : nums) {
            if (num == null) {
                continue;
            }
            result = result.add(num);
        }
        return result;
    }
}
