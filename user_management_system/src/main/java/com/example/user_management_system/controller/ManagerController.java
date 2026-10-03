package com.example.user_management_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_management_system.dto.TaskRequest;
import com.example.user_management_system.dto.TaskResponse;
import com.example.user_management_system.dto.UserWithTasksResponse;
import com.example.user_management_system.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final TaskService taskService;

    @GetMapping("/users")
    public ResponseEntity<List<UserWithTasksResponse>> getUsersWithTasks() {
        return ResponseEntity.ok(taskService.getAllUsersWithTasks());
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> assignTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.assignTask(request));
    }
}