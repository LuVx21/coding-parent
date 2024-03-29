package org.luvx.coding.common;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CallCmd {
    public static List<String> callShell(String... shellString) {
        log.debug("执行命令:{}", Arrays.toString(shellString));
        List<String> result = Lists.newArrayList();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(shellString);

            try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    result.add(line);
                }
            }

            try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = input.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                if (!sb.isEmpty()) {
                    throw new RuntimeException(sb.toString());
                }
            }

            int exitValue = process.waitFor();
            if (0 != exitValue) {
                throw new RuntimeException(STR."调用失败. 错误码:\{exitValue}");
            }
        } catch (Throwable e) {
            log.error("调用失败", e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        List<String> resultList = callShell("ls", "-l");
        for (String line : resultList) {
            System.out.println(line);
        }
    }
}