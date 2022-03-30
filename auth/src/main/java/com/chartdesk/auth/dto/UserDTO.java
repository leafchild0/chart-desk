package com.chartdesk.auth.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * DTO for user
 *
 * @date 02.03.2022
 * @author vmalyshev
 */
@Data
@Builder
public class UserDTO
{
	@NotBlank
	private String username;

	@NotBlank
	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	@Email
	private String email;

	private Boolean isAdmin;
}
