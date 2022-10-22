package org.luvx.coding.common.more;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
public class MoreChecker {
    @Getter
    private final List<String> result = Lists.newLinkedList();

    private MoreChecker() {
    }

    public static MoreChecker of() {
        return new MoreChecker();
    }

    public MoreChecker addCheckNull(Object value, String msg) {
        return checkIf(() -> value == null, msg);
    }

    public MoreChecker addCheckEmpty(Object value, String msg) {
        return checkIf(() -> ObjectUtils.isEmpty(value), msg);
    }

    public MoreChecker addCheckBlank(String value, String msg) {
        return checkIf(() -> StringUtils.isBlank(value), msg);
    }

    public MoreChecker addCheckEquals(Object value, Object compareValue, String msg) {
        return checkIf(() -> Objects.equals(value, compareValue), msg);
    }

    public MoreChecker checkIf(boolean flag, String msg) {
        if (flag) {
            result.add(msg);
        }
        return this;
    }

    public MoreChecker checkIf(boolean flag, Supplier<String> msg) {
        if (flag) {
            result.add(msg.get());
        }
        return this;
    }

    public MoreChecker checkIf(Supplier<Boolean> predicate, String msg) {
        return checkIf(predicate, () -> msg);
    }

    public MoreChecker checkIf(Supplier<Boolean> predicate, Supplier<String> msg) {
        if (predicate.get()) {
            result.add(msg.get());
        }
        return this;
    }

    public MoreChecker check() {
        if (CollectionUtils.isNotEmpty(result)) {
            throw new RuntimeException(String.join("|", result));
        }
        return this;
    }
}

