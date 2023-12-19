package org.luvx.coding.common.zip;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class ZipUtils {
    public static void unzip(String filePath) {
        File source = new File(filePath);
        if (!source.exists()) {
            return;
        }
        String dirName = Files.getNameWithoutExtension(filePath);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {
            ZipEntry entry = null;
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
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));) {
                    int read = 0;
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
