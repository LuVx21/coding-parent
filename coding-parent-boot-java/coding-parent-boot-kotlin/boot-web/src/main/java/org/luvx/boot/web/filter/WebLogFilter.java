package org.luvx.boot.web.filter;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebLogFilter extends OncePerRequestFilter implements Ordered {

    private int order = Ordered.LOWEST_PRECEDENCE - 8;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("filter:{}", getClass().getName());
        MDC.clear();
        MDC.put("trace_id", UUID.randomUUID().toString().replaceAll("-", ""));
        ContentCachingRequestWrapper wrapperRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrapperResponse = new ContentCachingResponseWrapper(response);

        String remoteInfo = getRemoteInfo(request);
        log.debug("request from: {}", remoteInfo);
        String urlParams = getRequestParams(request);
        log.info("request params: {}", urlParams);
        String requestBodyStr = getRequestBody(wrapperRequest);
        if (StringUtils.isNotEmpty(requestBodyStr)) {
            log.info("request body: {}", requestBodyStr);
        }

        filterChain.doFilter(wrapperRequest, wrapperResponse);

        String responseBodyStr = getResponseBody(wrapperResponse);
        if (JSON.isValid(responseBodyStr)) {
            log.info("response body: {}", responseBodyStr);
        }

        wrapperResponse.copyBodyToResponse();
    }

    public static String getRemoteInfo(HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        String remoteAddr = request.getRemoteAddr();
        String remoteHost = request.getRemoteHost();
        int remotePort = request.getRemotePort();
        return Lists.newArrayList(remoteUser, remoteAddr, remoteHost, remotePort).toString();
    }

    /**
     * 获取请求地址上的参数
     *
     * @param request
     * @return
     */
    public static String getRequestParams(HttpServletRequest request) {
        String type = request.getMethod(), uri = request.getRequestURI();
        Enumeration<String> enu = request.getParameterNames();
        String query = "";
        if (enu.hasMoreElements()) {
            query = STR."?\{Streams.stream(new EnumerationIterator<>(enu))
                    .map(name -> STR."\{name}=\{request.getParameter(name)}")
                    .collect(Collectors.joining("&"))}";
        }

        return STR."\{type} \{uri}\{query}";
    }

    /**
     * 请求参数
     *
     * @param request
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);

        if (wrapper != null) {
            byte[] buffer = wrapper.getContentAsByteArray();
            String characterEncoding = wrapper.getCharacterEncoding();
            String body = getPayLoad(buffer, characterEncoding);
            return body.replaceAll("\\n", "");
        }
        return "";
    }

    /**
     * 响应参数
     *
     * @param response
     */
    private String getResponseBody(ContentCachingResponseWrapper response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper == null) {
            return "";
        }

        byte[] buffer = wrapper.getContentAsByteArray();
        String characterEncoding = wrapper.getCharacterEncoding();
        return getPayLoad(buffer, characterEncoding);
    }

    /**
     * byte[] -> str
     *
     * @param buf
     * @param characterEncoding 中文有问题
     * @return
     */
    private String getPayLoad(byte[] buf, String characterEncoding) {
        return (buf == null || buf.length == 0) ? "" : new String(buf, StandardCharsets.UTF_8);
    }
}