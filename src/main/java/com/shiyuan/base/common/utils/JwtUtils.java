package com.shiyuan.base.common.utils;

import com.shiyuan.base.modules.auth.JwtUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        // 使用固定的密钥字符串
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(JwtUserInfo jwtUserInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", jwtUserInfo.getId());
        claims.put("username", jwtUserInfo.getUsername());
        claims.put("role", jwtUserInfo.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(jwtUserInfo.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtUserInfo parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        JwtUserInfo jwtUserInfo = new JwtUserInfo();
        jwtUserInfo.setId(claims.get("id", Long.class));
        jwtUserInfo.setUsername(claims.get("username", String.class));
        jwtUserInfo.setRole(claims.get("role", String.class));

        return jwtUserInfo;
    }
}