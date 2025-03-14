package org.luvx.boot.nosql.redis;

import org.junit.jupiter.api.extension.ExtendWith;
import org.luvx.boot.common.util.ApplicationContextUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.annotation.Resource;

@EnableAutoConfiguration
@SpringBootConfiguration
@ComponentScan(basePackages = "org.luvx.boot")

@SpringBootTest(classes = ApplicationContextUtils.class)
@ExtendWith(SpringExtension.class)
public class BaseTest {
    @Resource
    protected RedisTemplate<String, Object> redisTemplate;
    @Resource
    protected StringRedisTemplate           stringRedisTemplate;
}
