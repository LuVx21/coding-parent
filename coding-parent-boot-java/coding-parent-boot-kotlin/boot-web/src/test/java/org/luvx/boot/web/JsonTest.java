package org.luvx.boot.web;

import org.junit.jupiter.api.Test;
import org.luvx.boot.web.enums.CommonStatusEnum;
import org.luvx.coding.common.more.MorePrints;
import org.luvx.coding.common.util.JsonUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

class JsonTest {

    @Test
    void m1() {
        String json = "{\"status\":2}";
        A a1 = JsonUtils.fromJson(json, A.class);
        CommonStatusEnum type = a1.status;
        MorePrints.println(type, type.getCode());
    }

    @Test
    void m2() {
        A a = new A();
        a.setStatus(CommonStatusEnum.INVALID);
        String s = JsonUtils.toJson(a);
        System.out.println(s);
    }

    @Getter
    @Setter
    @ToString
    private static class A {
        private CommonStatusEnum status;
    }
}