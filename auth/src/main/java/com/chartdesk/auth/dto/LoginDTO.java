package com.chartdesk.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for login
 *
 * @author vmalyshev
 */
@Data
public class LoginDTO {
	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
