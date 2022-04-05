package com.chartdesk.auth.model;

import com.chartdesk.auth.dto.UserDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String username;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotNull
	private boolean enabled;

	private boolean isAdmin;

	public User(String username, String firstName, String lastName, String email, String password) {

		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = true;
		this.isAdmin = false;
	}

	public UserDTO toDto() {

		return UserDTO.builder()
			.email(email)
			.username(username)
			.isAdmin(isAdmin)
			.firstName(firstName)
			.lastName(lastName)
			.id(id)
			.build();
	}
}
