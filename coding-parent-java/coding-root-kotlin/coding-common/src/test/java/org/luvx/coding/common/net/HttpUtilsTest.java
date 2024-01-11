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

    @Test
    void m2() {
        String url = "https://hosts.gitcdn.top/hosts.txt";
        var fileName = UrlUtils.urlFileName(url);
        System.out.println(fileName);
    }
}