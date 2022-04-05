package com.chartdesk.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for sign up
 *
 * @date 18.02.2022
 * @author vmalyshev
 */
@Data
public class SignUpDTO
{
	@NotBlank
	private String username;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;
}
