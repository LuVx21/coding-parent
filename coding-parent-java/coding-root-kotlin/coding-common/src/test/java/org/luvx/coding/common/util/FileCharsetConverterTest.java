package org.luvx.coding.common.util;

import org.junit.jupiter.api.Test;

class FileCharsetConverterTest {
    @Test
    void main(String[] args) throws Exception {
        String path = "test.txt";
        FileCharsetConverter.convert(path, "GBK", "UTF-8", (dir, name) -> name.endsWith("txt"));
    }
}