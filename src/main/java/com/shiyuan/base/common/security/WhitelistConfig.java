package com.shiyuan.base.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class WhitelistConfig {
    @Bean
    public List<String> permitAllPaths() {
        return List.of(
            "/auth/login",
            "/auth/register",
            "/auth/forgetPassword",
            "/file/**",
            "/doc.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/static/**",
            "/resources/**",
            "/images/**"
        );
    }
}
