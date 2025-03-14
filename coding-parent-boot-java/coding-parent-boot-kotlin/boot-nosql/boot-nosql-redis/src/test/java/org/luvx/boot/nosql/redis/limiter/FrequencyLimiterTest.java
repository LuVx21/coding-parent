package org.luvx.boot.nosql.redis.limiter;

import org.junit.jupiter.api.Test;
import org.luvx.boot.nosql.redis.BaseTest;

class FrequencyLimiterTest extends BaseTest {

    @Test
    void m1() {
        if (LimiterBizEnum.NOTIFY.acquire()) {
            System.out.println("notify");

            String string = stringRedisTemplate.opsForValue().get(LimiterBizEnum.keyPrefix + ":" + LimiterBizEnum.NOTIFY.getKey());
            System.out.println(string);
        }
    }
}