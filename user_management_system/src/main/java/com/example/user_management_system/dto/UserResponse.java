package com.example.user_management_system.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.user_management_system.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String name;
	private String email;
	private Set<String> roles;
	// NO password field. That is the whole point.

	public static UserResponse from(User user) {
		return UserResponse.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.roles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet())).build();
	}
}
