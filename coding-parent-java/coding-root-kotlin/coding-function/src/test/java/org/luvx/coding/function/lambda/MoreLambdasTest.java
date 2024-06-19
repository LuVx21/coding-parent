package org.luvx.coding.function.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoreLambdasTest {
    @Test
    void m00() {
        String hello = MoreLambdas.cast("hello", String::toUpperCase);
        Assertions.assertEquals("HELLO", hello);
    }
}