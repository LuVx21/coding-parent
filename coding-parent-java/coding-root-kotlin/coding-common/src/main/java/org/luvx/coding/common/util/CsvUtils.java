package org.luvx.coding.common.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CsvUtils {
    public static List<String> csvPick(String filePath, Predicate<String> filter, Function<String, String> mapper) throws Exception {
        File file = new File(filePath);
        return Files.asCharSource(file, Charsets.UTF_8)
                .readLines()
                .stream()
                .filter(filter)
                .map(mapper)
                .collect(Collectors.toList());
    }
}
