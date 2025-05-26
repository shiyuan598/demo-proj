package com.shiyuan.base;

import com.shiyuan.base.common.security.JwtUserInfo;
import com.shiyuan.base.common.utils.JwtUtils;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void contextLoads() {}

    @Test
    public void getSecret() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(keyBytes);
        String secret = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated JWT Secret: " + secret);
    }

    @Test
    public void getToken() {
        JwtUserInfo jwtUserInfo = new JwtUserInfo();
        jwtUserInfo.setId(1L);
        jwtUserInfo.setUsername("zhangsan");
        jwtUserInfo.setRole("admin");
        List<String> roles = List.of(new String[] {"ROLE_admin"});
        jwtUserInfo.setAuthorities(roles);

        String token = jwtUtils.generateToken(jwtUserInfo);
        System.out.println("Generated JWT Token: " + token);
    }
}
