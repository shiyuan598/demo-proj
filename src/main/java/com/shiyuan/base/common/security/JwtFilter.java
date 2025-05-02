package com.shiyuan.base.common.security;

import com.shiyuan.base.common.utils.JwtUtils;
import com.shiyuan.base.modules.auth.JwtUserInfo;
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
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        JwtUserInfo jwtUserInfo = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                jwtUserInfo = jwtUtils.parseToken(jwt); // 👈 改成返回 LoginUser
            } catch (ExpiredJwtException e) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT Token 已过期");
                return;
            } catch (JwtException e) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "无效的 JWT Token");
                return;
            }
        }

        if (jwtUserInfo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 构造权限
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(jwtUserInfo.getRole())
            );

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