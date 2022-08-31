package org.luvx.common.util;

import org.junit.jupiter.api.Test;
import org.luvx.common.more.MorePrints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
}