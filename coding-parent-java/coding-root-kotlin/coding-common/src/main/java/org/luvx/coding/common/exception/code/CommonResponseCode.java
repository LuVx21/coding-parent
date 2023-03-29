package org.luvx.coding.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.luvx.coding.common.exception.base.BizResponseCode;

@Getter
@AllArgsConstructor
public enum CommonResponseCode implements BizResponseCode {
    SUCCESS("0", "成功"),
    FAILED("1", "失败"),
    SERVER_ERROR("2", ""),
    OPERATION_FAILED("10001", "系统错误，操作失败"),
    ;

    /**
     * 返回码
     */
    private final String code;
    /**
     * 返回消息
     */
    private final String message;
}
