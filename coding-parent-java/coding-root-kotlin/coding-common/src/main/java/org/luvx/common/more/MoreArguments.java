package org.luvx.common.more;

import org.apache.commons.lang3.ArrayUtils;

public class MoreArguments {
    public static <T> T[] varArgument(T... ts) {
        return ts;
    }

    /**
     * 可变参数按数量分组
     */
    public static Object[][] doArgs(int cnt, Object... args) {
        if (cnt == 0 || ArrayUtils.isEmpty(args)) {
            return new Object[0][0];
        }
        if (args.length % cnt != 0) {
            throw new RuntimeException("参数数量不匹配");
        }
        Object[][] result = new Object[args.length / cnt][cnt];
        for (int i = 0; i < args.length; i++) {
            result[i / cnt][i % cnt] = args[i];
        }
        return result;
    }
}
