package com.example.user_management_system.dto;

import java.util.Set;

import com.example.user_management_system.entity.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

	@NotBlank(message = "Name is Required")
	@Size(min = 2, max = 40, message = "Name must be 2-40 characters")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password is required")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$", message = "Password must be at least 8 characters with uppercase, lowercase, digit and special character")
	private String password;

	@NotEmpty(message = "At least one role is required")
	private Set<RoleName> roles;
}
