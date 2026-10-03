package com.example.user_management_system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_management_system.dto.TaskRequest;
import com.example.user_management_system.dto.TaskResponse;
import com.example.user_management_system.dto.UserResponse;
import com.example.user_management_system.dto.UserWithTasksResponse;
import com.example.user_management_system.entity.Task;
import com.example.user_management_system.entity.User;
import com.example.user_management_system.exception.ResourceNotFoundException;
import com.example.user_management_system.repository.TaskRepository;
import com.example.user_management_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // Manager: assign a task to a user
    @Transactional
    public TaskResponse assignTask(TaskRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignedTo(user);

        return TaskResponse.from(taskRepository.save(task));
    }

    // Manager: all users with their tasks
    @Transactional(readOnly = true)
    public List<UserWithTasksResponse> getAllUsersWithTasks() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserWithTasksResponse(
                        UserResponse.from(user),
                        taskRepository.findByAssignedToId(user.getId())
                                .stream()
                                .map(TaskResponse::from)
                                .toList()))
                .toList();
    }

    // User: only my own tasks
    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return taskRepository.findByAssignedToId(user.getId())
                .stream()
                .map(TaskResponse::from)
                .toList();
    }
}