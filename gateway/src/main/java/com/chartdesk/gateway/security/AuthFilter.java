package com.chartdesk.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * @author vmalyshev
 */
@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	public static final String PREFIX = "Bearer ";
	private final JwtTokenProvider tokenProvider;

	public AuthFilter(JwtTokenProvider tokenProvider) {
		super(Config.class);
		this.tokenProvider = tokenProvider;
	}

	private String getJwtFromRequest(ServerHttpRequest request) {

		String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				throw new RuntimeException("Missing authorization information");
			}

			String token = getJwtFromRequest(exchange.getRequest());
			return StringUtils.hasText(token) && tokenProvider.validateToken(token) ? chain.filter(exchange) : Mono.empty();
		};
	}

	public static class Config {
		// empty class as I don't need any particular configuration
	}
}
