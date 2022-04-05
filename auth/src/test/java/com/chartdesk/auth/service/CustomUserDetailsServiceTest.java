package com.chartdesk.auth.service;

import com.chartdesk.auth.dto.SignUpDTO;
import com.chartdesk.auth.model.User;
import com.chartdesk.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author vmalyshev
 */
class CustomUserDetailsServiceTest {

	CustomUserDetailsService service;
	@Mock UserRepository repository;
	@Mock PasswordEncoder encoder;

	@BeforeEach
	void setUp() {

		MockitoAnnotations.openMocks(this);
		service	= new CustomUserDetailsService(repository, encoder);
	}

	@Test
	void createNewUser() {

		SignUpDTO dto = new SignUpDTO();
		dto.setEmail("test@email.com");
		dto.setFirstName("John");
		dto.setLastName("Doe");
		dto.setUsername("username");
		dto.setPassword("password");

		User mocked = new User("username", "John", "Doe", "test@email.com", "password");

		when(repository.save(any(User.class))).thenReturn(mocked);
		User user = service.createNewUser(dto).block();
		assertNotNull(user);
		assertEquals(mocked, user);

	}

	@Test
	void findByUsername() {

		User mocked = new User("username", "John", "Doe", "test@email.com", "password");

		when(repository.findByUsername(anyString())).thenReturn(Optional.of(mocked));
		UserDetails details = service.findByUsername("username").block();
		assertEquals("username", details.getUsername());
		assertEquals("password", details.getPassword());

	}

	@Test
	void findByUserId() {
		User mocked = new User("username", "John", "Doe", "test@email.com", "password");
		mocked.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(mocked));
		User details = service.findByUserId(1L).get();
		assertEquals("username", details.getUsername());
		assertNull(details.getPassword());
	}

	@Test
	void findAllUsers() {

		User mocked = new User("username", "John", "Doe", "test@email.com", "password");

		when(repository.findByEnabledTrue()).thenReturn(Collections.singletonList(mocked));
		List<User> users = service.findAllUsers();
		assertEquals(1, users.size());
		assertEquals(mocked, users.get(0));
	}

	@Test
	void updateUser() {

		User mocked = new User("username", "John", "Doe", "test@email.com", "password");
		mocked.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(mocked));
		when(repository.save(mocked)).thenReturn(mocked);
		User details = service.updateUser(mocked.toDto()).get();
		assertEquals("username", details.getUsername());
		assertNull(details.getPassword());

	}

	@Test
	void disableUser() {

		User mocked = new User("username", "John", "Doe", "test@email.com", "password");
		mocked.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(mocked));
		mocked.setEnabled(false);
		when(repository.save(mocked)).thenReturn(mocked);
		User details = service.updateUser(mocked.toDto()).get();
		assertFalse(details.isEnabled());
	}
}
