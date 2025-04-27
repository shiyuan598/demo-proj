package com.shiyuan.base;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootTest
class DemoProjApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void getSecret() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] keyBytes = new byte[32]; // 256 bits
		secureRandom.nextBytes(keyBytes);
		String secret = Base64.getEncoder().encodeToString(keyBytes);
		System.out.println("Generated JWT Secret: " + secret);
	}

}
