package org.luvx.boot.common.spel;

import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import jakarta.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachingExpressionParser extends SpelExpressionParser {
    private final Map<String, SpelExpression> cache = new ConcurrentHashMap<>();

    @Nonnull
    @Override
    protected SpelExpression doParseExpression(@Nonnull String expressionString, ParserContext context) throws ParseException {
        return this.cache.computeIfAbsent(expressionString, k -> super.doParseExpression(k, context));
    }
}
