package org.luvx.coding.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;
import org.luvx.coding.common.more.MoreRuns;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
class JsonUtilsTest {

    @Test
    void parseArray() {
        User user = new User();
        user.setUserId(10000L);
        user.setUserName("foo");
        user.setPassWord("bar");
        user.setAge(18);
        user.setValid(2);
        MorePrints.println(
                JsonUtils.toJson(user),
                JsonUtils.toJsonSnake(user)
        );

        String json = """
                [{
                "userId" : 10000
                },
                {
                "userId" : 10001
                }
                ]
                """;
        List<User> users = JsonUtils.parseArray(json, List.class, User.class);
        System.out.println(users.getClass());
        System.out.println(users.get(0).getClass());

        json = """
                {
                "userId" : 10000
                }
                """;
        Map<String, Long> map = JsonUtils.fromJson(json, TreeMap.class, Long.class);
        MorePrints.println(
                map.getClass(),
                map
        );

        Map map1 = JsonUtils.fromJson(json, Map.class);
        MorePrints.println(
                map1.getClass(),
                map1
        );
    }

    @Test
    void diffTest() {
        String json1 = """
                {
                "a1" : 1,
                "b" : 2,
                "c" : 33
                }
                """;
        String json2 = """
                {
                "a2" : 1,
                "b" : 2,
                "c" : 333
                }
                """;

        MoreRuns.runWithTime(() -> {
            Triple<Set<String>, Set<String>, Map<String, Map.Entry<Object, Object>>> diff = JsonUtils.diff(json1, json2);
            log.info("json1独有key:{}, json2独有key:{}, 不同值:{}", diff.getLeft(), diff.getMiddle(), diff.getRight());
        });
    }
}