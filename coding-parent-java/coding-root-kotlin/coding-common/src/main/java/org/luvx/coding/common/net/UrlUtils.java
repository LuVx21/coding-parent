package org.luvx.coding.common.net;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class UrlUtils {

    public static String urlFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    /**
     * 参数map转请求字符串
     * 若map为null返回 空字符串""
     *
     * @param params 参数map
     */
    public static String mapToQueryString(Map<String, String> params) {
        if (MapUtils.isEmpty(params)) {
            return "";
        }
        return params.entrySet().stream()
                .map(entry -> {
                    String v = entry.getValue(), value = isNotEmpty(v) ? URLEncoder.encode(v, UTF_8) : "";
                    return String.join("=", entry.getKey(), value);
                })
                .collect(Collectors.joining("&"));
    }

    /**
     * 构造Get请求的utl
     *
     * @param url    请求地址
     * @param params 请求参数map,无需urlEncode
     */
    public static String getUrlWithParam(String url, Map<String, String> params) {
        Map<String, String> pMap = Optional.ofNullable(params)
                // 改为HashMap,防止外部传入的是一个不可变Map
                .map(HashMap::new)
                .orElse(Maps.newHashMap());
        String urlPrefix = Optional.of(url)
                .map(String::strip)
                .map(s -> s.split("\\?"))
                .map(strings -> {
                    if (strings.length > 2) {
                        throw new RuntimeException("url中存在多个?:" + url);
                    }
                    if (strings.length == 2) {
                        String paramStr = strings[1];
                        if (paramStr.contains("=")) {
                            Splitter.MapSplitter mapSplitter = Splitter.on("&").withKeyValueSeparator("=");
                            try {
                                pMap.putAll(mapSplitter.split(paramStr));
                            } catch (Exception e) {
                                throw new RuntimeException("解析错误:" + paramStr);
                            }
                        } else {
                            // 存在没有=的情况,一般用作特殊用途,添加=反而会有问题
                            // 此时params的Map正常情况是不需要传递参数的
                            strings[1] = URLEncoder.encode(paramStr, UTF_8);
                            return String.join("?", strings);
                        }
                    }
                    return strings[0];
                })
                .orElseThrow();

        if (pMap.isEmpty()) {
            return urlPrefix;
        }
        return pMap.entrySet().stream()
                .map(entry -> {
                    String v = entry.getValue(), value = isNotEmpty(v) ? URLEncoder.encode(v, UTF_8) : "";
                    return String.join("=", URLEncoder.encode(entry.getKey(), UTF_8), value);
                })
                .collect(Collectors.joining("&", urlPrefix + "?", ""));
    }

    public static String urlAddDomain(String baseUrl, String urlWithoutDomain) {
        if (!urlWithoutDomain.startsWith("http")) {
            URI uri = URI.create(baseUrl);
            String domain = STR."\{uri.getScheme()}://\{uri.getHost()}";
            urlWithoutDomain = domain + urlWithoutDomain;
        }
        return urlWithoutDomain;
    }
}







