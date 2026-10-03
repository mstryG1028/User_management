package com.example.user_management_system.dto;

import java.util.Set;

import com.example.user_management_system.entity.RoleName;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AssignRolesRequest {
	@NotEmpty(message = "At least one role is required")
	private Set<RoleName> roles;

}
