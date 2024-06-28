package org.luvx.coding.common.file;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class MappedFileReaderTest {
    @Test
    @SneakyThrows
    void readTest() {
        try (MappedFileReader reader = new MappedFileReader("xxx", 65536)) {
            while (reader.read() != -1) {
                byte[] array = reader.getArray();
            }
        }
    }
}