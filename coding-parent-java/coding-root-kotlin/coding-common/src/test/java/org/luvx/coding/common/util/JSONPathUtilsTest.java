package org.luvx.coding.common.util;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import lombok.ToString;
import lombok.*;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;
import org.luvx.coding.common.more.MoreRuns;

class JSONPathUtilsTest {
    String json = """
            {
                "id": 191475333,
                "node_id": "MDEwOlJlcG9zaXRvcnkxOTE0NzUzMzM=",
                "name": "BaseMapper"
            }
            """;

    @Test
    void m1() {
        String path = "$.name";
        MoreRuns.runWithTime(() -> {
            JSONPath of = JSONPath.of(path);
            for (int i = 0; i < 10000; i++) {
                of.extract(JSONReader.of(json));
            }
        });

        MoreRuns.runWithTime(() -> {
            for (int i = 0; i < 10000; i++) {
                JSONPathUtils.get(json, path);
            }
        });

        MoreRuns.runWithTime(() -> {
            for (int i = 0; i < 10000; i++) {
                JSONPath.extract(json, path);
            }
        });

        MorePrints.println();
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Entity {
        private Integer id;
        private String  name;
        private Object  value;
    }
}