package org.luvx.coding.common.util;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

public class FileUtils {

    public static FileCopyResult retryCopy(
            final ByteSource byteSource,
            final File outFile,
            final Predicate<Throwable> shouldRetry,
            final int maxRetryTimes
    ) {
        try {
            StreamUtils.retryCopy(
                    byteSource,
                    Files.asByteSink(outFile),
                    shouldRetry,
                    maxRetryTimes
            );
            return new FileCopyResult(outFile);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static class FileCopyResult {
        private final Collection<File> files = Lists.newArrayList();
        @Getter
        private       long             size  = 0L;

        public FileCopyResult(File... files) {
            this(ObjectUtils.isEmpty(files) ? Collections.emptyList() : Arrays.asList(files));
        }

        public FileCopyResult(Collection<File> files) {
            this.addSizedFiles(files);
        }

        protected void addSizedFiles(Collection<File> files) {
            if (ObjectUtils.isEmpty(files)) {
                return;
            }
            long size = 0L;
            for (File file : files) {
                size += file.length();
            }
            this.files.addAll(files);
            this.size += size;
        }

        public Collection<File> getFiles() {
            return ImmutableList.copyOf(files);
        }

        public void addFiles(Collection<File> files) {
            this.addSizedFiles(files);
        }

        public void addFile(File file) {
            this.addFiles(Collections.singletonList(file));
        }
    }
}
