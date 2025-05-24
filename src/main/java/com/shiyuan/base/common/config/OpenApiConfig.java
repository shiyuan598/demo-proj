package com.shiyuan.base.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfig {

    // JWT 安全方案名称（统一使用常量避免硬编码）
    private static final String SECURITY_SCHEME_NAME = "Authorization";

    // 不需要认证的白名单路径（只影响 API 文档的 token 输入框显示，权限控制在 SecurityConfig 配置）
    private static final String[] EXCLUDED_PATHS = {"/auth/**"};

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 配置 OpenAPI 文档的基本信息及全局安全方案
     */
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
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME) // 推荐使用常量，避免与上方不一致
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)))
                .security(Collections.singletonList(
                        new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                );
    }

    /**
     * 根据请求路径动态决定是否在文档中显示 token 输入框
     * 白名单路径将不会显示 Authorization 输入框
     */
    @Bean
    public OperationCustomizer operationCustomizer(RequestMappingHandlerMapping handlerMapping) {
        Map<HandlerMethod, RequestMappingInfo> methodInfoMap = new HashMap<>();
        handlerMapping.getHandlerMethods().forEach((info, method) -> methodInfoMap.put(method, info));

        return (Operation operation, HandlerMethod handlerMethod) -> {
            RequestMappingInfo mappingInfo = methodInfoMap.get(handlerMethod);
            if (mappingInfo != null) {
                Set<String> paths = extractPatterns(mappingInfo);
                if (!paths.isEmpty()) {
                    String path = paths.iterator().next();

                    boolean isExcluded = Arrays.stream(EXCLUDED_PATHS)
                            .anyMatch(pattern -> pathMatcher.match(pattern, path));

                    // 白名单不设置 security 字段（不显示 token），否则显示 token 输入框
                    operation.setSecurity(isExcluded
                            ? Collections.emptyList()
                            : Collections.singletonList(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)));
                }
            }
            return operation;
        };
    }

    /**
     * 提取接口的路径表达式，兼容 Spring Boot 2.x 和 3.x 的 RequestMappingInfo 实现
     */
    private Set<String> extractPatterns(RequestMappingInfo info) {
        if (info.getPathPatternsCondition() != null) {
            // Spring Boot 3.x
            return info.getPathPatternsCondition()
                    .getPatterns()
                    .stream()
                    .map(PathPattern::getPatternString)
                    .collect(Collectors.toSet());
        } else if (info.getPatternsCondition() != null) {
            // Spring Boot 2.x
            return info.getPatternsCondition().getPatterns();
        }
        return Collections.emptySet();
    }
}
