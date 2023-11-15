package org.luvx.coding.common.net;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class HttpUtilsTest {

    @Test
    void m1() throws Exception {
        HttpUtils.HttpResult<Path> download = HttpUtils.download("https://hosts.gitcdn.top/hosts.txt",
                null, null, 0
        );
        System.out.println(download);
    }
}