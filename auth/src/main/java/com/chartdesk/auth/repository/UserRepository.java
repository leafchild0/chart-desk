package com.chartdesk.auth.repository;

import java.util.Optional;

import com.chartdesk.auth.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
	Optional<User> findByUsername(String username);
}
