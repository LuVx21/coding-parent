package org.luvx.boot.common.spel;

import java.util.regex.Pattern;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpelParserUtils {
    private static final Pattern left  = Pattern.compile("\\{");
    private static final Pattern right = Pattern.compile("\\}");

    private static final ExpressionParser parser = new SpelExpressionParser();

    public static Expression parse(String expression) {
        try {
            return parser.parseExpression(expression);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String parse2String(String expression, EvaluationContext context) {
        String exp = getString(expression);
        return parse(exp).getValue(context).toString();
    }

    /**
     * <pre>
     *     {#a}【连接{#b}】{#c}({#d}）
     *     ↓
     *     ''+#a+'【连接'+#b+'】'+#c+'('+#d+'）'
     * </pre>
     */
    public static String getString(String expression) {
        String exp = "'" + expression + "'";
        exp = left.matcher(exp).replaceAll("'+");
        exp = right.matcher(exp).replaceAll("+'");
        log.info("{} -> {}", expression, exp);
        return exp;
    }
}
