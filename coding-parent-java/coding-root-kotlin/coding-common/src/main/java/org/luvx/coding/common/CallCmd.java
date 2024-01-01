package org.luvx.coding.common;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class CallCmd {
    public static void main(String[] args) throws Exception {
        List<String> resultList = callShell("ls -l");
        for (String line : resultList) {
            System.out.println(line);
        }
    }

    public static List<String> callShell(String shellString) {
        log.info("执行命令:{}", shellString);
        List<String> result = Lists.newArrayList();
        try {
            Process process = Runtime.getRuntime().exec(shellString);
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.error("调用失败. 错误码:{}", exitValue);
            }
            try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    result.add(line);
                }
            }
        } catch (Throwable e) {
            log.error("调用失败", e);
        }
        return result;
    }
}