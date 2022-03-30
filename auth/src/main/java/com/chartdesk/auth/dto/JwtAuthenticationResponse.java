package com.chartdesk.auth.dto;

/**
 * Response with token
 *
 * @author vmalyshev
 * @date 18.02.2022
 */
public class JwtAuthenticationResponse {
	private String accessToken;
	private String tokenType = "Bearer";

	public JwtAuthenticationResponse(String accessToken) {

		this.accessToken = accessToken;
	}

	public String getAccessToken() {

		return accessToken;
	}

	public void setAccessToken(String accessToken) {

		this.accessToken = accessToken;
	}

	public String getTokenType() {

		return tokenType;
	}

	public void setTokenType(String tokenType) {

		this.tokenType = tokenType;
	}
}
