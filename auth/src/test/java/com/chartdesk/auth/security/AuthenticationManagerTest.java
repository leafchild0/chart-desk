package com.chartdesk.auth.security;

import com.chartdesk.auth.jwt.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author vmalyshev
 */
class AuthenticationManagerTest {

	AuthenticationManager manager;
	JwtTokenUtil util = mock(JwtTokenUtil.class);

	@BeforeEach
	void setUp() {
		manager = new AuthenticationManager(util);

		Claims claims = mock(Claims.class);
		when(claims.get("authorities", List.class)).thenReturn(Collections.singletonList("USER"));

		when(util.getSubject(anyString())).thenReturn("user");
		when(util.validateUserToken(anyString())).thenReturn(true);
		when(util.getTokenClaims(anyString())).thenReturn(claims);
	}

	@Test
	void authenticate() {

		Authentication authentication = new TestingAuthenticationToken("user", "password");
		Authentication result = manager.authenticate(authentication).block();
		assertNotNull(result);
		assertTrue(result.isAuthenticated());
		assertEquals("user", result.getPrincipal());
		assertEquals(1, result.getAuthorities().size());
	}
}
