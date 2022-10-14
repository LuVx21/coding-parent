package org.luvx.boot.common.spel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.regex.Pattern;

@Slf4j
public class SpelParserUtils {
    private static final Pattern left  = Pattern.compile("\\{");
    private static final Pattern right = Pattern.compile("\\}");

    private static final ExpressionParser parser = new SpelExpressionParser();

    public static Expression parse(String expression) {
        return parser.parseExpression(expression);
    }

    public static Object parse(String expression, EvaluationContext context) {
        return parser.parseExpression(expression).getValue(context);
    }

    public static Object parseWithReplace(String expression, EvaluationContext context) {
        String exp = getString(expression);
        log.info("{} -> {}", expression, exp);
        return parser.parseExpression(exp).getValue(context);
    }

    /**
     * <pre>
     *     {#a}【连接{#b}】{#c} ({#d}）
     *     ↓
     *     ''+#a+'【连接'+#b+'】'+#c+' ('+#d+'）'
     * </pre>
     */
    public static String getString(String expression) {
        expression = "'" + expression + "'";
        expression = left.matcher(expression).replaceAll("'+");
        expression = right.matcher(expression).replaceAll("+'");
        return expression;
    }
}
