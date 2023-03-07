package org.luvx.coding.manifold.extensions.java.lang.String;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.apache.commons.lang3.StringUtils;

@Extension
public class StringExt {
    public static String[] split(@This String str, char separator) {
        return StringUtils.split(str, separator);
    }
}