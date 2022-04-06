package com.chartdesk.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


/**
 * DTO for user
 *
 * @author vmalyshev
 * @date 02.03.2022
 */
@Data
@Builder
public class UserDTO {
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
