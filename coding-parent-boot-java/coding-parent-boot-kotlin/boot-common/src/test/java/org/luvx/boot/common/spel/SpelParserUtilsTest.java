package org.luvx.boot.common.spel;

import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

class SpelParserUtilsTest {
    @Test
    void m1() {
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("a", "foo");
        context.setVariable("b", "bar");
        context.setVariable("c", 1121);
        context.setVariable("d", "哈哈");

        String expression = "{#a}【连接{#b}】{#c} ({#d}）";
        Object value = SpelParserUtils.parse(expression, context);
        System.out.println(value);
    }

    @Test
    void m2() {
        Expression message = SpelParserUtils.parse("'Hello World'.concat('!')");
        System.out.println(message.getValue());

        Expression exp = SpelParserUtils.parse("'Hello World'.length()");
        Integer size = exp.getValue(Integer.class);
        System.out.println(size);
    }
}