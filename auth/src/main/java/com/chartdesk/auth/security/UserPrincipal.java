package com.chartdesk.auth.security;

import com.chartdesk.auth.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@EqualsAndHashCode
public class UserPrincipal implements UserDetails
{
	private final Long id;

	private final String username;

	@JsonIgnore
	private final String password;

	private final Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public static Optional<UserDetails> create(Optional<User> data)
	{
		return data.map(user -> new UserPrincipal(
				user.getId(),
				user.getUsername(),
				user.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority( user.isAdmin() ? "ADMIN": "USER"))
		));
	}

	@Override
	public String getUsername()
	{
		return username;
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}
}
