package com.chartdesk.auth.service;

import com.chartdesk.auth.dto.SignUpDTO;
import com.chartdesk.auth.model.User;
import com.chartdesk.auth.repository.UserRepository;
import com.chartdesk.auth.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

/**
 * Custom user service
 *
 * @date 22.02.2022
 * @author vmalyshev
 */
@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService
{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	private boolean existsByUsername(String username)
	{
		return userRepository.findByUsername(username).isPresent();
	}

	public Mono<User> createNewUser(SignUpDTO registerDTO)
	{
		if (existsByUsername(registerDTO.getUsername())) {
			throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "User with this username already exists");
		}
		User user = new User(registerDTO.getUsername(),
			registerDTO.getEmail(), passwordEncoder.encode(registerDTO.getPassword()));
		return Mono.just(userRepository.save(user));
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return Mono.justOrEmpty(UserPrincipal.create(userRepository.findByUsername(username)));
	}
}
