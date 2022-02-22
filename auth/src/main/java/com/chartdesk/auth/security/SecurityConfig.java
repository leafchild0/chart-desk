package com.chartdesk.auth.security;

import com.chartdesk.auth.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig
{
	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	SecurityContextRepository securityContextRepository;

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
						Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
				)
				.accessDeniedHandler((swe, e) ->
						Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
				).and()
				.authenticationManager(authenticationManager)
				.securityContextRepository(securityContextRepository)
				.csrf().disable().authorizeExchange()
				// Allow login and register
				.pathMatchers("/login", "/register").permitAll()
				// Allow all for actuator
				.pathMatchers("/actuator/**").permitAll()
				.anyExchange().authenticated()
				.and()
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
