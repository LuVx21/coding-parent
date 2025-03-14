package org.luvx.boot.nosql.redis.limiter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;
import org.luvx.boot.common.util.ApplicationContextUtils;
import org.luvx.boot.nosql.redis.utils.RedisUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
@ToString
public class FrequencyLimiter {
    private String                      keyPrefix;
    private DefaultRedisScript<Integer> redisScript;

    public FrequencyLimiter(String keyPrefix) {
        this.keyPrefix = keyPrefix;
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Integer.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("countDownTo.lua")));
    }

    public boolean decrTimesInDay(String identifier, int down, long times) {
        return decrInDay(identifier, down, 0, times);
    }

    public boolean decrInDay(String identifier, int down, long min, long max) {
        return decr(identifier, down, min, max, Duration.ofDays(1));
    }

    public boolean decr(String key, int down, long min, long max, Duration exp) {
        key = RedisUtils.key(keyPrefix, key);
        RedisTemplate client = ApplicationContextUtils.getBean("redisTemplate");
        Object r = client.execute(redisScript, List.of(key), down, min, max, exp.toSeconds());
        return NumberUtils.toInt(r.toString()) == 1;
    }
}
