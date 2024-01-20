package org.luvx.coding.common.util;

import com.google.common.base.Throwables;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import org.luvx.coding.common.retryer.RetryUtils;

import java.io.*;
import java.time.Duration;
import java.util.function.Predicate;

public class StreamUtils {
    public static long copyToFileAndClose(InputStream is, File file) throws IOException {
        file.getParentFile().mkdirs();
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
            final long result = ByteStreams.copy(is, os);
            os.flush();
            return result;
        } finally {
            is.close();
        }
    }

    public static long copyAndClose(InputStream is, OutputStream os) throws IOException {
        try {
            final long retval = ByteStreams.copy(is, os);
            os.flush();
            return retval;
        } finally {
            is.close();
            os.close();
        }
    }

    public static long retryCopy(
            final ByteSource byteSource,
            final ByteSink byteSink,
            final Predicate<Throwable> shouldRetry,
            final int maxRetryTimes
    ) {
        try {
            Long r = RetryUtils.supplyWithRetry(
                    "文件复制重试",
                    () -> {
                        try (InputStream inputStream = byteSource.openStream()) {
                            try (OutputStream outputStream = byteSink.openStream()) {
                                final long retval = ByteStreams.copy(inputStream, outputStream);
                                outputStream.flush();
                                return retval;
                            }
                        }
                    },
                    shouldRetry,
                    maxRetryTimes,
                    Duration.ofSeconds(1),
                    null
            );
            return r == null ? 0 : r;
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
