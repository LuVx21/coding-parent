package org.luvx.coding.common.util;

import com.google.common.html.HtmlEscapers;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class TextUtils {
    private static final Pattern INVALID_TEXT_PATTERN = Pattern
            .compile("\\<(script|iframe).*?\\>|\\<\\/(script|iframe)\\>", Pattern.CASE_INSENSITIVE);

    public static boolean xssFilter(String text) {
        if (StringUtils.isBlank(text)) {
            return true;
        }
        return !INVALID_TEXT_PATTERN.matcher(text).find();
    }

    /**
     * html标签转义
     */
    public static String htmlEscape(String text) {
        return HtmlEscapers.htmlEscaper().escape(text);
    }
}
