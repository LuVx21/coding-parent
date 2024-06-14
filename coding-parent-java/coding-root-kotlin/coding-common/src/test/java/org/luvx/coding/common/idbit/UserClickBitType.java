package org.luvx.coding.common.idbit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.luvx.coding.common.idbit.dto.HasBitInfo;

@Getter
@AllArgsConstructor
public enum UserClickBitType implements HasBitInfo {
    UNKNOWN(-1, "未知"),
    E0(0, "社交勋章气泡"),
    E1(1, "已参与1"),
    E2(2, "已参与2"),
    E62(62, "已参与62"),
    E63(63, "已参与63"),
    ;

    private final int    bitIndex;
    private final String name;

    @Override
    public int bitIndex() {
        return getBitIndex();
    }
}
