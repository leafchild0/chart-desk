package com.chartdesk.gateway;

import com.chartdesk.gateway.security.JwtTokenAuthenticationFilter;
import com.chartdesk.gateway.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

/**
 * Security config for the gateway
 *
 * @author vmalyshev
 */
@EnableWebFluxSecurity
public class SecurityConfig {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                // Disable session
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) ->
                        Mono.fromRunnable(() -> {
                            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        })
                )
                .accessDeniedHandler((swe, e) ->
                        Mono.fromRunnable(() -> {
                            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        })
                ).and()
                .addFilterAfter(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.CORS)
                .csrf().disable().authorizeExchange()
                // Allow login and register
                .pathMatchers("/auth/login", "/auth/register").permitAll()
                // Allow all for actuator
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .build();
    }
}

