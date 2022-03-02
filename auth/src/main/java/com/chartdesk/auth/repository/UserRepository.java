package com.chartdesk.auth.repository;

import java.util.List;
import java.util.Optional;

import com.chartdesk.auth.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
	/**
	 * Find user by userName
	 */
	Optional<User> findByUsername(String username);

	/**
	 * Find all Active users
	 *
	 */
	List<User> findByEnabledTrue();
}
