package com.shiyuan.base.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;

@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME = "Authorization";
    // 定义不需要api-doc上显示请求头Authorization输入框的路径数组(文档层面的配置，SecurityConfig中仍需要配置permitAll)
    private static final String[] EXCLUDED_PATHS = {"/auth/**"};
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Demo Project API")
                        .version("1.0")
                        .description("Demo Project API Documentation")
                        .contact(new Contact()
                                .name("wangshiyuan")
                                .email("wangyuanhpu@163.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                // 配置安全方案
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .in(SecurityScheme.In.HEADER)
                                        .bearerFormat("JWT")
                                        .scheme("bearer")
                        ))
                // 设置全局的安全要求，即所有接口默认都需要进行安全验证
                .security(Collections.singletonList(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)));
    }

    /**
     * 配置操作自定义器，用于对每个 API 操作进行自定义处理
     * @param requestMappingHandlerMapping Spring 的请求映射处理器映射器
     * @return 配置好的操作自定义器
     */
    @Bean
    public OperationCustomizer operationCustomizer(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        // 获取所有请求映射信息
        Map<HandlerMethod, RequestMappingInfo> handlerMethodMappingInfoMap = getRequestMappingInfo(requestMappingHandlerMapping);

        return (Operation operation, HandlerMethod handlerMethod) -> {
            try {
                RequestMappingInfo mappingInfo = handlerMethodMappingInfoMap.get(handlerMethod);
                if (mappingInfo != null) {
                    Set<String> patterns = getPatterns(mappingInfo);
                    if (!patterns.isEmpty()) {
                        String path = patterns.iterator().next();
                        setSecurityForPath(operation, path);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return operation;
        };
    }

    private Map<HandlerMethod, RequestMappingInfo> getRequestMappingInfo(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Map<HandlerMethod, RequestMappingInfo> handlerMethodMappingInfoMap = new HashMap<>();
        requestMappingHandlerMapping.getHandlerMethods().forEach((info, method) -> handlerMethodMappingInfoMap.put(method, info));
        return handlerMethodMappingInfoMap;
    }

    /**
     * 根据路径判断是否需要进行安全验证，并设置相应的安全要求
     * @param operation 当前的 API 操作
     * @param path 当前操作的路径
     */
    private void setSecurityForPath(Operation operation, String path) {
        boolean isExcluded = Arrays.stream(EXCLUDED_PATHS)
                .anyMatch(excludedPath -> pathMatcher.match(excludedPath, path));
        operation.setSecurity(isExcluded ? Collections.emptyList() : Collections.singletonList(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)));
    }

    private Set<String> getPatterns(RequestMappingInfo mappingInfo) throws Exception {
        Set<String> patterns = new HashSet<>();
        if (mappingInfo.getClass().getMethod("getPatternValues") != null) {
            Method method = mappingInfo.getClass().getMethod("getPatternValues");
            Object result = method.invoke(mappingInfo);
            if (result instanceof Set) {
                patterns = (Set<String>) result;
            }
        } else {
            Method patternsConditionMethod = mappingInfo.getClass().getMethod("getPatternsCondition");
            Object patternsCondition = patternsConditionMethod.invoke(mappingInfo);
            if (patternsCondition != null) {
                Method patternsMethod = patternsCondition.getClass().getMethod("getPatterns");
                Object result = patternsMethod.invoke(patternsCondition);
                if (result instanceof Set) {
                    patterns = (Set<String>) result;
                }
            }
        }
        return patterns;
    }
}