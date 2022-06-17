package org.luvx.common.more;

import javax.annotation.Nullable;
import java.util.List;

public class MoreCollections {
    @Nullable
    public static <T> T first(List<T> es) {
        if (es == null || es.isEmpty()) {
            return null;
        }
        return es.get(0);
    }
}
