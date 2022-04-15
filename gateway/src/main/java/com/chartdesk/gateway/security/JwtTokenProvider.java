package com.chartdesk.gateway.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

/**
 * Provider for JWT tokens
 * Manages, generate and validate tokens
 *
 * @author vmalyshev
 */
@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${auth-service.jwtSecret}")
	private String jwtSecret;

	private SecretKey getKey() {

		return Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	public boolean validateToken(String authToken) {

		try {
			Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(authToken);

			return true;
		}
		catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		}
		catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		}
		catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		}
		catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}
}
