package com.example.user_management_system.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithTasksResponse {

	private UserResponse user;
	private List<TaskResponse> tasks;
}
