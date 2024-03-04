package org.luvx.coding.common.consts;

import com.github.phantomthief.util.MoreFunctions;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
public class Commons {
    public static RateLimiter getLimiter(String url) {
        RateLimiter r = MoreFunctions.catching(() -> {
            URI uri = URI.create(url);
            int port = uri.getPort();
            String host = port != -1 ? STR."\{uri.getHost()}:\{port}" : uri.getHost();
            return Common.RATE_LIMITER_SUPPLIER.get().get(host);
        });
        return r != null ? r : RateLimiter.create(1);
    }
}
