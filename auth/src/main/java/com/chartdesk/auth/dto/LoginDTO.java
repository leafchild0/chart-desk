package com.chartdesk.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * DTO for login
 *
 * @author vmalyshev
 *
 */
@Data
public class LoginDTO
{
	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
