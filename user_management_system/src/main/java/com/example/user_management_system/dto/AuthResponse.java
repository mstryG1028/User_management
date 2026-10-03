package com.example.user_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

	private String token;
	private String type;

	public AuthResponse(String token) {
		this.token = token;
		this.type = "Bearer";
	}
}
