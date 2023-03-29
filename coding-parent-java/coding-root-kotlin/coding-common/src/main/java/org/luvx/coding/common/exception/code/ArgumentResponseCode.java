package org.luvx.coding.common.exception.code;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.luvx.coding.common.exception.base.BizResponseCode;

@Getter
@AllArgsConstructor
public enum ArgumentResponseCode implements BizResponseCode {
    BAD_REQUEST_MSG("40000", "客户端请求参数错误"),
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
