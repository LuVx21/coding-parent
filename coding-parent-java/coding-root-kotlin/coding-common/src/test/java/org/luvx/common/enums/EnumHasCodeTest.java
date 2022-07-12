package org.luvx.common.enums;

import org.junit.jupiter.api.Test;
import org.luvx.common.enums.use.Web;

class EnumHasCodeTest {
    @Test
    void m1() {
        // Map<Integer, Web> map = EnumUtils.enumMap(Web.class);
        // System.out.println(map);
        // Web of = EnumHasCode.of(Web.class, 2);
        // System.out.println(of.ordinal());

        boolean validCode = EnumHasCode.isValidCode(Web.class, 0);
        System.out.println(validCode);
    }
}