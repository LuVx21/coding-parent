package org.luvx.coding.common.json;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MoreRuns;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

class JsonAbbrTest {
    String json = """
            {
                "a": 1,
                "b": 2,
                "c": {
                    "d": 3,
                    "e": 4
                },
                "f": {
                    "g": 5,
                    "h": {
                        "i": 6,
                        "j": 7
                    }
                },
                "k": [
                    {
                        "l": 8,
                        "m": 9
                    },
                    {
                        "n": 10
                    },
                    [11, 12,{"o": 13,"p": 14},[15,16]],
                    17
                ]
            }
            """;

    @Test
    void m1() throws Exception {
//        CharSource charSource = Files.asCharSource(new File(""), Charsets.UTF_8);
//        String json = charSource.read();
        MoreRuns.runWithTime(() -> {
            Map<String, Object> result = JsonAbbr.jsonAbbr(json);
            result.entrySet().stream()
                    .sorted(Entry.comparingByKey())
                    .forEach(System.out::println);
        });
    }

    @Test
    void m2() {
        MoreRuns.runWithTime(() -> {
            Map<String, Object> result = JsonAbbr.jsonAbbr("[" + json + "]");
            result.entrySet().stream()
                    .sorted(Entry.comparingByKey())
                    .forEach(System.out::println);
        });
    }
}