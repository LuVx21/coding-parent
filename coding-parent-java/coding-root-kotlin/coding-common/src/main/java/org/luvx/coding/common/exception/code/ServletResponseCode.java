package org.luvx.coding.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.luvx.coding.common.exception.base.BizResponseCode;

@Getter
@AllArgsConstructor
public enum ServletResponseCode implements BizResponseCode {
    ;
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;
}
