package com.chartdesk.auth.service;

import com.chartdesk.auth.dto.SignUpDTO;
import com.chartdesk.auth.dto.UserDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Custom user service
 *
 * @author vmalyshev
 * @date 22.02.2022
 */
@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	private boolean existsByUsername(String username) {

		return userRepository.findByUsername(username).isPresent();
	}

	/**
	 * Create new user from passed dto
	 *
	 * @param registerDTO - dto with data
	 * @return - newly created user
	 */
	public Mono<User> createNewUser(SignUpDTO registerDTO) {

		if (existsByUsername(registerDTO.getUsername())) {
			throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "User with this username already exists");
		}
		User user = new User(registerDTO.getUsername(), registerDTO.getFirstName(), registerDTO.getLastName(),
			registerDTO.getEmail(), passwordEncoder.encode(registerDTO.getPassword()));
		return Mono.just(userRepository.save(user));
	}

	/**
	 * Finds user by name, used in auth
	 *
	 * @param username - username to find
	 * @return user details
	 */
	@Override
	public Mono<UserDetails> findByUsername(String username) {

		return Mono.justOrEmpty(UserPrincipal.create(userRepository.findByUsername(username)));
	}

	/**
	 * Find user by passed id, should be long
	 */
	public Optional<User> findByUserId(Long userId) {

		return userRepository.findById(userId).map(u -> {
				u.setPassword(null);
				return u;
			}
		);
	}

	/**
	 * Find all users for admin purposes
	 *
	 * @return all users
	 */
	public List<User> findAllUsers() {

		return userRepository.findByEnabledTrue();
	}

	/**
	 * Updates user with passed data with id
	 */
	public Optional<User> updateUser(UserDTO user) {

		return findByUserId(user.getId()).map(found -> {
			found.setEmail(user.getEmail());
			found.setUsername(user.getUsername());

			return userRepository.save(found);
		});
	}

	/**
	 * Disables user by passed id
	 */
	public Optional<User> disableUser(Long userId) {

		return findByUserId(userId).map(found -> {
			found.setEnabled(false);

			return userRepository.save(found);
		});
	}
}
