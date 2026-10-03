package com.example.user_management_system.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_management_system.dto.AssignRolesRequest;
import com.example.user_management_system.dto.CreateUserRequest;
import com.example.user_management_system.dto.UpdateUserRequest;
import com.example.user_management_system.dto.UserResponse;
import com.example.user_management_system.entity.Role;
import com.example.user_management_system.entity.RoleName;
import com.example.user_management_system.entity.User;
import com.example.user_management_system.exception.DuplicateResourceException;
import com.example.user_management_system.exception.ResourceNotFoundException;
import com.example.user_management_system.repository.RoleRepository;
import com.example.user_management_system.repository.TaskRepository;
import com.example.user_management_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    // ---------- CREATE ----------
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(resolveRoles(request.getRoles()));

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    // ---------- READ ----------
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return UserResponse.from(findUserOrThrow(id));
    }

    @Transactional(readOnly = true)
    public UserResponse getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserResponse.from(user);
    }

    // ---------- UPDATE ----------
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = findUserOrThrow(id);

        boolean emailChanged = !user.getEmail().equalsIgnoreCase(request.getEmail());
        if (emailChanged && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return UserResponse.from(userRepository.save(user));
    }

    // ---------- ASSIGN ROLES ----------
    @Transactional
    public UserResponse assignRoles(Long id, AssignRolesRequest request) {

        User user = findUserOrThrow(id);
        user.setRoles(resolveRoles(request.getRoles()));

        return UserResponse.from(userRepository.save(user));
    }

    // ---------- DELETE ----------
    @Transactional
    public void deleteUser(Long id) {

        User user = findUserOrThrow(id);

        // tasks point to this user through a foreign key, so remove them first
        taskRepository.deleteAll(taskRepository.findByAssignedToId(id));

        userRepository.delete(user);
    }

    // ---------- HELPERS ----------
    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private Set<Role> resolveRoles(Set<RoleName> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (RoleName roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            roles.add(role);
        }
        return roles;
    }
}