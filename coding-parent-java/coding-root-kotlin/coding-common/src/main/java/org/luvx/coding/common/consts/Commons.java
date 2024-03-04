package org.luvx.coding.common.consts;

import com.github.phantomthief.util.MoreFunctions;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
public class Commons {
    public static RateLimiter getLimiter(String url) {
        URI uri = URI.create(url);
        int port = uri.getPort();
        return getLimiter(uri.getHost(), port);
    }

    public static RateLimiter getLimiter(String host, int port) {
        String s = port <= 0 ? host : STR."\{host}:\{port}";
        RateLimiter r = MoreFunctions.catching(() -> Common.RATE_LIMITER_SUPPLIER.get().get(s));
        return r != null ? r : RateLimiter.create(1);
    }
}
