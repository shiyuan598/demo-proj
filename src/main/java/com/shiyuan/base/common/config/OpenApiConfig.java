package com.shiyuan.base.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
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
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME = "Authorization";
    // 定义不需要api-doc上显示请求头Authorization输入框的路径数组(文档层面的配置，SecurityConfig中仍需要配置permitAll)
    private static final String[] EXCLUDED_PATHS = {"/auth/**"};
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 配置 OpenAPI 文档基本信息和全局安全配置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Demo Project API").version("1.0").description("Demo Project API Documentation").contact(new Contact().name("wangshiyuan").email("wangyuanhpu@163.com")).license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                // 设置全局的安全要求，即所有接口默认都需要进行安全验证
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme().name("Authorization").type(SecurityScheme.Type.HTTP).in(SecurityScheme.In.HEADER).bearerFormat("JWT").scheme("bearer"))).security(Collections.singletonList(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)));
    }


    /**
     * 自定义 OperationCustomizer，根据接口路径是否在白名单中动态设置是否显示安全认证输入框
     */
    @Bean
    public OperationCustomizer operationCustomizer(RequestMappingHandlerMapping handlerMapping) {
        // 建立 HandlerMethod 到 RequestMappingInfo 的映射
        Map<HandlerMethod, RequestMappingInfo> methodInfoMap = new HashMap<>();
        handlerMapping.getHandlerMethods().forEach((info, method) -> methodInfoMap.put(method, info));

        return (operation, handlerMethod) -> {
            RequestMappingInfo mappingInfo = methodInfoMap.get(handlerMethod);
            if (mappingInfo != null) {
                Set<String> paths = extractPatterns(mappingInfo);
                if (!paths.isEmpty()) {
                    String path = paths.iterator().next();
                    // 判断该路径是否为白名单，决定是否添加认证信息
                    boolean isExcluded = Arrays.stream(EXCLUDED_PATHS).anyMatch(pattern -> pathMatcher.match(pattern, path));

                    // 设置 security 字段：空表示不显示 Authorization，非空表示需要认证
                    if (!isExcluded) {
                        operation.setSecurity(Collections.singletonList(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)));
                    } else {
                        operation.setSecurity(Collections.emptyList());
                    }
                }
            }
            return operation;
        };
    }

    /**
     * 提取 RequestMappingInfo 中的路径，兼容 Spring Boot 2.x 和 3.x
     */
    private Set<String> extractPatterns(RequestMappingInfo info) {
        // Spring Boot 3 使用 PathPatternsCondition
        if (info.getPathPatternsCondition() != null) {
            return info.getPathPatternsCondition().getPatterns().stream().map(PathPattern::getPatternString).collect(Collectors.toSet());
        }
        // Spring Boot 2 使用 PatternsCondition
        else if (info.getPatternsCondition() != null) {
            return info.getPatternsCondition().getPatterns();
        }
        return Collections.emptySet();
    }
}

//全局统一使用 JWT 授权机制，每个接口都默认要求 Authorization。
//灵活排除无需授权的接口（如登录、注册），让 API 文档更清晰、更合理。
// OperationCustomizer是Springdoc OpenAPI提供的“钩子函数”，用来在 OpenAPI 文档生成时，定制每一个接口（operation）的文档信息。比如：
//给某些接口加上安全认证说明（如 Authorization）
//给特定接口添加描述、标签、响应示例等
//动态修改接口的请求参数或返回信息