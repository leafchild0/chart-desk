package com.chartdesk.auth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

}
