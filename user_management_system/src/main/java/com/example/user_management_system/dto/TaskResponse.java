package com.example.user_management_system.dto;

import com.example.user_management_system.entity.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
	private Long id;
	private String title;
	private String description;
	private String status;
	private Long assignedToId;
	private String assignedToName;

	public static TaskResponse from(Task task) {
		return TaskResponse.builder().id(task.getId()).title(task.getTitle()).description(task.getDescription())
				.status(task.getStatus()).assignedToId(task.getAssignedTo().getId())
				.assignedToName(task.getAssignedTo().getName()).build();
	}
}
