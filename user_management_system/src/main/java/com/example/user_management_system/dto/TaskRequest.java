package com.example.user_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
	@NotBlank(message = "Title is required")
	@Size(max = 100, message = "Title can be at most 100 characters")
	private String title;

	@Size(max = 500, message = "Description can be at most 500 characters")
	private String description;

	@NotNull(message = "User id is required")
	private Long userId;
}
