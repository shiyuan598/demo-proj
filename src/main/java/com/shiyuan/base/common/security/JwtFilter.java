package com.shiyuan.base.common.security;

import com.shiyuan.base.common.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    // 从请求头中提取 JWT 令牌；验证 JWT 令牌的有效性，包括签名验证、过期时间检查等
    // 从 JWT 令牌中解析出用户信息和权限，将用户信息和权限封装到 Authentication 对象中，并将其设置到 SecurityContextHolder 中
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        JwtUserInfo jwtUserInfo = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                jwtUserInfo = jwtUtils.parseToken(jwt);
            } catch (ExpiredJwtException e) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT Token 已过期");
                return;
            } catch (JwtException e) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "无效的 JWT Token");
                return;
            } catch (Exception e) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT Token 处理异常");
                return;
            }
        }

        if (jwtUserInfo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 构造权限
            List<GrantedAuthority> authorities = jwtUserInfo.getAuthorities()
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // 构造 Authentication
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(jwtUserInfo, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String errorJson = "{\"message\": \"" + message + "\"}";
        response.getWriter().write(errorJson);
    }
}
