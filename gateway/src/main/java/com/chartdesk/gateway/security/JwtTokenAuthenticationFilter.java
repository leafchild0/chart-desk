package com.chartdesk.gateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter that initiates token validation, works for all routes ONLY
 *
 * @author vmalyshev
 */
public class JwtTokenAuthenticationFilter implements WebFilter {
    public static final String PREFIX = "Bearer ";
    private final JwtTokenProvider tokenProvider;

    public JwtTokenAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    private String getJwtFromRequest(ServerHttpRequest request) {

        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = getJwtFromRequest(exchange.getRequest());
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            try {

                Claims claims = tokenProvider.getClaims(token);
                Long userId = tokenProvider.getUserIdFromJWT(claims);

                @SuppressWarnings("unchecked")
                List<String> authorities = claims.get("authorities", List.class);

                if (userId != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null,
                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ignore) {
                SecurityContextHolder.clearContext();
            }
        }
        return chain.filter(exchange);
    }
}
