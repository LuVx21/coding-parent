package org.luvx.boot.web.tools;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.luvx.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ListAllUrlPath {
    @Autowired
    private WebApplicationContext context;

    @SneakyThrows
    @PostConstruct
    public void init() {
        RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        Map<String, Object> result = Maps.newHashMap();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            PathPatternsRequestCondition pattern = info.getPathPatternsCondition();
            if (CollectionUtils.isEmpty(methods)
                    || pattern == null
                    || CollectionUtils.isEmpty(pattern.getPatterns())
            ) {
                continue;
            }
            Method method = entry.getValue().getMethod();
            String s = method.getDeclaringClass().getName() + "#" + method.getName();
            List<String> methodList = methods.stream().map(RequestMethod::toString).collect(Collectors.toList());
            pattern.getPatternValues()
                    .forEach(url -> result.put(url, Map.of("location", s, "method", methodList)));
        }
        log.info("获取所有url:\n{}", JsonUtils.toJson(result));
    }
}
