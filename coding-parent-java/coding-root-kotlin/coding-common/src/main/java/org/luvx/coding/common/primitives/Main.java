package org.luvx.coding.common.primitives;

import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.luvx.coding.common.reflect.Reflects;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * byte boolean short char int long float double
 */
public class Main {
    final String  main     = "";
    final Pattern compile0 = Pattern.compile("<firstUpper>"), compile1 = Pattern.compile("<baseType>");

    record Option(String path,
                  String name,
                  List<String> types,
                  List<String> template
    ) {
    }

    @SneakyThrows
    public void main(String[] args) {
        byte[] bytes = Files.readAllBytes(Path.of(Reflects.projectPath("gen/template.json").getPath()));
        List<Option> options = JSON.parseArray(new String(bytes), Option.class);

        for (Option option : options) {
            Files.createDirectories(Path.of(main, option.path));
            String template = String.join("\n", option.template);
            for (String baseType : option.types) {
                String firstUpper = StringUtils.capitalize(baseType);
                String s = compile0.matcher(template).replaceAll(firstUpper);
                s = compile1.matcher(s).replaceAll(baseType);
                Files.writeString(Path.of(main, option.path, compile0.matcher(option.name).replaceAll(firstUpper)), s);
            }
        }
    }
}
