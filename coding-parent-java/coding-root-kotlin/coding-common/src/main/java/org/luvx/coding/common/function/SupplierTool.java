package org.luvx.coding.common.function;

import com.github.phantomthief.util.MoreSuppliers.CloseableSupplier;

import java.net.http.HttpClient;
import java.time.Duration;

import static com.github.phantomthief.util.MoreSuppliers.lazy;

public class SupplierTool {
    public static final CloseableSupplier<HttpClient> httpClientSupplier = lazy(() -> {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(5_000))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    });
}
