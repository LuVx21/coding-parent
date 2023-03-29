package org.luvx.coding.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.luvx.coding.common.exception.base.BizResponseCode;

@Getter
@AllArgsConstructor
public enum LicenceResponseCode implements BizResponseCode {
    /**
     * Bad licence type
     */
    BAD_LICENCE_TYPE("7001", "Bad licence type."),
    /**
     * Licence not found
     */
    LICENCE_NOT_FOUND("7002", "Licence not found.");

    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;
}