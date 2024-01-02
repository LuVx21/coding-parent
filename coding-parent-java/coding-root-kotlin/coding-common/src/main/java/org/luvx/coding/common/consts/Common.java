package org.luvx.coding.common.consts;

import com.github.phantomthief.util.MoreSuppliers.CloseableSupplier;
import com.google.common.base.Splitter;
import com.google.common.util.concurrent.RateLimiter;

import static com.github.phantomthief.util.MoreSuppliers.lazy;

public interface Common {
    Splitter SPLITTER_COMMA   = Splitter.on(",");
    Splitter SPLITTER_NEWLINE = Splitter.on("\n");

    CloseableSupplier<RateLimiter> RATE_LIMITER_SUPPLIER = lazy(() -> RateLimiter.create(1));
}
