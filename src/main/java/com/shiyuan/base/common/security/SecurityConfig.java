package com.shiyuan.base.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 请求到 Spring Boot 应用时，请求首先会到达 Spring Security 的过滤器链
    // SecurityConfig 类中配置的 securityFilterChain 定义了请求的访问规则和过滤器的执行顺序
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(cors -> cors.configurationSource(corsConfigurationSource())) // 启用跨域配置
            .authorizeHttpRequests(auth -> auth
                // 放行登录接口
                .requestMatchers("/auth/login", "/auth/register", "/auth/forgetPassword").permitAll()
                .requestMatchers("/file/upload", "/file/download").permitAll()
                // 放行 Knife4j 和 Swagger 文档相关路径
                .requestMatchers("/doc.html", // Knife4j 文档页面
                    "/webjars/**", // Knife4j 依赖的资源
                    "/v3/api-docs/**", // OpenAPI 规范数据
                    "/swagger-resources/**", // Swagger 资源
                    "/swagger-ui/**", // Swagger UI 界面
                    "/swagger-ui.html" // Swagger UI 页面
                ).permitAll()
                // 放行静态资源路径
                .requestMatchers("/static/**", "/resources/**", "/images/**").permitAll().anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许所有来源的跨域请求
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 允许所有请求方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        // 允许所有请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 允许携带凭证（如 Cookie）
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口都有效
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
