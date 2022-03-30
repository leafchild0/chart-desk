package com.chartdesk.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Token provider
 *
 * @author vmalyshev
 * @date 22.02.2022
 */
@Component
@Slf4j
public class JwtTokenUtil {
	@Value("${auth-service.jwtSecret}")
	private String jwtSecret;

	@Value("${auth-service.jwtExpirationInMs}")
	private int jwtExpirationInMs;

	public String generateToken(UserDetails user) {

		Instant now = Instant.now();
		return Jwts.builder()
			.setSubject(user.getUsername())
			.claim("authorities", user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(now.plusSeconds(jwtExpirationInMs)))
			.signWith(getKey())
			.compact();
	}

	public String getUserIdFromJWT(String token) {

		Claims claims = Jwts.parserBuilder()
			.setSigningKey(getKey())
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.getSubject();
	}

	public Claims getAllClaims(String token) {

		return Jwts.parserBuilder()
			.setSigningKey(getKey())
			.build()
			.parseClaimsJws(token)
			.getBody();

	}

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
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}
}
