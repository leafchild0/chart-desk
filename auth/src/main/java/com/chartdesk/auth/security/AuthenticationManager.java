package com.chartdesk.auth.security;

import com.chartdesk.auth.jwt.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vmalyshev
 * @date 22.02.2022
 */
@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JwtTokenUtil jwtTokenUtil;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtTokenUtil.getSubject(authToken);
        return Mono.just(jwtTokenUtil.validateUserToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> {
                    Claims claims = jwtTokenUtil.getTokenClaims(authToken);
                    List<String> rolesMap = claims.get("authorities", List.class);
                    return new UsernamePasswordAuthenticationToken(username, null, rolesMap.stream().map(SimpleGrantedAuthority::new).toList());
                });
    }
}
