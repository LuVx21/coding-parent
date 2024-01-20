package org.luvx.coding.common.zip;

import com.google.common.base.Throwables;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.luvx.coding.common.retryer.RetryUtils;
import org.luvx.coding.common.util.FileUtils;
import org.luvx.coding.common.util.FileUtils.FileCopyResult;
import org.luvx.coding.common.util.Predicates;
import org.luvx.coding.common.util.StreamUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Enumeration;
import java.util.function.Predicate;
import java.util.zip.*;

@Slf4j
public class ZipUtils {
    private static final int    DEFAULT_RETRY_COUNT = 3;
    private static final String GZ_SUFFIX           = ".gz";
    private static final String ZIP_SUFFIX          = ".zip";

    public static boolean isZip(String name) {
        return StringUtils.isNotEmpty(name)
                && name.length() > ZIP_SUFFIX.length()
                && name.endsWith(ZIP_SUFFIX);
    }

    public static boolean isGz(String name) {
        return StringUtils.isNotEmpty(name)
                && name.length() > GZ_SUFFIX.length()
                && name.endsWith(GZ_SUFFIX);
    }

    public static long zip(File directory, File outputZipFile) throws IOException {
        return zip(directory, outputZipFile, false);
    }

    public static long zip(File directory, File outputZipFile, boolean fsync) throws IOException {
        if (!isZip(outputZipFile.getName())) {
            log.warn(STR."No .zip suffix[\{outputZipFile}], putting files from [\{directory}] into it anyway.");
        }

        try (final FileOutputStream out = new FileOutputStream(outputZipFile)) {
            long bytes = zip(directory, out);
            if (fsync) {
                out.getChannel().force(true);
            }

            return bytes;
        }
    }

    public static long zip(File directory, OutputStream out) throws IOException {
        if (!directory.isDirectory()) {
            throw new RuntimeException(STR."文件[\{directory}]不是文件夹");
        }

        final ZipOutputStream zipOut = new ZipOutputStream(out);

        long totalSize = 0;
        for (File file : directory.listFiles()) {
            log.info("添加文件[{}(大小:{})]. 现在总大小{}", file, file.length(), totalSize);
            if (file.length() >= Integer.MAX_VALUE) {
                zipOut.finish();
                throw new RuntimeException(STR."file\{file} too large \{file.length()}");
            }
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            totalSize += Files.asByteSource(file).copyTo(zipOut);
        }
        zipOut.closeEntry();
        zipOut.flush();
        zipOut.finish();

        return totalSize;
    }

    @Nullable
    public static FileCopyResult unzip(
            final ByteSource byteSource,
            final File outDir,
            final Predicate<Throwable> shouldRetry,
            boolean cacheLocally
    ) throws IOException {
        if (!cacheLocally) {
            try {
                return RetryUtils.supplyWithRetry(
                        "解压缩zip重试",
                        () -> unzip(byteSource.openStream(), outDir),
                        shouldRetry,
                        DEFAULT_RETRY_COUNT,
                        Duration.ofSeconds(5),
                        null
                );
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        } else {
            final File tmpFile = File.createTempFile("compressionUtilZipCache", ZIP_SUFFIX);
            try {
                FileUtils.retryCopy(
                        byteSource,
                        tmpFile,
                        shouldRetry,
                        DEFAULT_RETRY_COUNT
                );
                return unzip(tmpFile, outDir);
            } finally {
                if (!tmpFile.delete()) {
                    log.warn("Could not delete zip cache at {}", tmpFile);
                }
            }
        }
    }

    public static FileCopyResult unzip(
            final ByteSource byteSource,
            final File outDir,
            boolean cacheLocally
    ) throws IOException {
        return unzip(byteSource, outDir, Predicates.IS_EXCEPTION, cacheLocally);
    }

    public static FileCopyResult unzip(final File pulledFile, final File outDir) throws IOException {
        if (!(outDir.exists() && outDir.isDirectory())) {
            throw new RuntimeException(
                    // "outDir[%s] must exist and be a directory", outDir
            );
        }
        log.info("Unzipping file{} to {}", pulledFile, outDir);
        final FileCopyResult result = new FileCopyResult();
        try (final ZipFile zipFile = new ZipFile(pulledFile)) {
            final Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                final ZipEntry entry = enumeration.nextElement();
                result.addFiles(
                        FileUtils.retryCopy(
                                new ByteSource() {
                                    @Nonnull
                                    @Override
                                    public InputStream openStream() throws IOException {
                                        return new BufferedInputStream(zipFile.getInputStream(entry));
                                    }
                                },
                                new File(outDir, entry.getName()),
                                Predicates.IS_EXCEPTION,
                                DEFAULT_RETRY_COUNT
                        ).getFiles()
                );
            }
        }
        return result;
    }

    public static FileCopyResult unzip(InputStream in, File outDir) throws IOException {
        try (final ZipInputStream zipIn = new ZipInputStream(in)) {
            final FileCopyResult result = new FileCopyResult();
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                final File file = new File(outDir, entry.getName());
                Files.asByteSink(file).writeFrom(zipIn);
                result.addFile(file);
                zipIn.closeEntry();
            }
            return result;
        }
    }

    public static FileCopyResult gunzip(final File pulledFile, File outFile) {
        return gunzip(Files.asByteSource(pulledFile), outFile);
    }

    public static FileCopyResult gunzip(InputStream in, File outFile) throws IOException {
        try (GZIPInputStream gzipInputStream = gzipInputStream(in)) {
            Files.asByteSink(outFile).writeFrom(gzipInputStream);
            return new FileCopyResult(outFile);
        }
    }

    public static GZIPInputStream gzipInputStream(final InputStream in) throws IOException {
        return new GZIPInputStream(
                new FilterInputStream(in) {
                    @Override
                    public int available() throws IOException {
                        final int otherAvailable = super.available();
                        return otherAvailable == 0 ? 1 << 10 : otherAvailable;
                    }
                }
        );
    }

    public static long gunzip(InputStream in, OutputStream out) throws IOException {
        try (GZIPInputStream gzipInputStream = gzipInputStream(in)) {
            final long result = ByteStreams.copy(gzipInputStream, out);
            out.flush();
            return result;
        } finally {
            out.close();
        }
    }

    public static FileCopyResult gunzip(
            final ByteSource in,
            final File outFile,
            Predicate<Throwable> shouldRetry
    ) {
        return FileUtils.retryCopy(
                new ByteSource() {
                    @Nonnull
                    @Override
                    public InputStream openStream() throws IOException {
                        return gzipInputStream(in.openStream());
                    }
                },
                outFile,
                shouldRetry,
                DEFAULT_RETRY_COUNT
        );
    }


    public static FileCopyResult gunzip(final ByteSource in, File outFile) {
        return gunzip(in, outFile, Predicates.IS_EXCEPTION);
    }

    public static long gzip(InputStream inputStream, OutputStream out) throws IOException {
        try (GZIPOutputStream outputStream = new GZIPOutputStream(out)) {
            final long result = ByteStreams.copy(inputStream, outputStream);
            out.flush();
            return result;
        } finally {
            inputStream.close();
        }
    }

    public static FileCopyResult gzip(final File inFile, final File outFile, Predicate<Throwable> shouldRetry) {
        gzip(Files.asByteSource(inFile), Files.asByteSink(outFile), shouldRetry);
        return new FileCopyResult(outFile);
    }

    public static long gzip(final ByteSource in, final ByteSink out, Predicate<Throwable> shouldRetry) {
        return StreamUtils.retryCopy(
                in,
                new ByteSink() {
                    @Nonnull
                    @Override
                    public OutputStream openStream() throws IOException {
                        return new GZIPOutputStream(out.openStream());
                    }
                },
                shouldRetry,
                DEFAULT_RETRY_COUNT
        );
    }


    public static FileCopyResult gzip(final File inFile, final File outFile) {
        return gzip(inFile, outFile, Predicates.IS_EXCEPTION);
    }

    public static String getGzBaseName(String name) {
        final String reducedName = Files.getNameWithoutExtension(name);
        if (isGz(name) && !reducedName.isEmpty()) {
            return reducedName;
        }
        throw new RuntimeException(STR."[\{name}] is not a valid gz file name");
    }

    public static void unzip(String filePath) {
        File source = new File(filePath);
        if (!source.exists()) {
            return;
        }
        String dirName = Files.getNameWithoutExtension(filePath);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                log.info("文件:{}", entry);
                File target = Path.of(source.getParent(), dirName, entry.getName()).toFile();
                if (!target.getParentFile().exists()) {
                    target.getParentFile().mkdirs();
                }

                // 写入文件
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target))) {
                    int read;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                }
            }
            zis.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
