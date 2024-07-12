package org.luvx.boot.nosql.redis.cache;

public record ElValue(
        String key,
        String hashKey,
        double score,
        double minScore,
        double maxScore
) {
}
