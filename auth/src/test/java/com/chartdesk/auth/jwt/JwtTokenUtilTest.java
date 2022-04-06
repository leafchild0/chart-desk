package com.chartdesk.auth.jwt;

import com.chartdesk.auth.model.User;
import com.chartdesk.auth.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author vmalyshev
 */
class JwtTokenUtilTest {

	JwtTokenUtil util;

	@BeforeEach
	void setUp() {
		util = new JwtTokenUtil();
		ReflectionTestUtils.setField(util, "jwtSecret", "secret-for-test-HUGE-veRy-t0p-sEcreTT");
		ReflectionTestUtils.setField(util, "jwtExpirationInMs", 1000);
	}

	@Test
	void generateUtil() throws InterruptedException {

		User u = new User();
		u.setId(1L);
		u.setUsername("test");
		u.setPassword("password");
		Optional<UserDetails> user = UserPrincipal.create(Optional.of(u));

		String result = util.generateToken(user.get());
		assertNotNull(result);
		assertEquals("test", util.getSubject(result));

		Claims claims = util.getTokenClaims(result);
		List<String> rolesMap = claims.get("authorities", List.class);
		assertNotNull(rolesMap);
		assertEquals(1, rolesMap.size());
		assertEquals("USER", rolesMap.get(0));

		assertTrue(util.validateUserToken(result));

	}
}
