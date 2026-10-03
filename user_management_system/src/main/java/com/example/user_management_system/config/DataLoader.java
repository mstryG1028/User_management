package com.example.user_management_system.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.user_management_system.entity.Role;
import com.example.user_management_system.entity.RoleName;
import com.example.user_management_system.entity.Task;
import com.example.user_management_system.entity.User;
import com.example.user_management_system.repository.RoleRepository;
import com.example.user_management_system.repository.TaskRepository;
import com.example.user_management_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final TaskRepository taskRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {

		// 1. Roles
		Role adminRole = createRoleIfMissing(RoleName.ADMIN);
		Role managerRole = createRoleIfMissing(RoleName.MANAGER);
		Role userRole = createRoleIfMissing(RoleName.USER);

		// 2. Users
		createUserIfMissing("Admin User", "admin@example.com", "Admin@123", Set.of(adminRole));
		User manager = createUserIfMissing("Manager User", "manager@example.com", "Manager@123",
				Set.of(managerRole, userRole));
		User ravi = createUserIfMissing("Ravi Kumar", "ravi@example.com", "Ravi@1234", Set.of(userRole));
		User sneha = createUserIfMissing("Sneha Patel", "sneha@example.com", "Sneha@123", Set.of(userRole));

		// 3. Tasks (only if table is empty)
		if (taskRepository.count() == 0) {
			taskRepository.save(Task.builder().title("Prepare monthly report")
					.description("Collect data and prepare the report").assignedTo(ravi).build());

			taskRepository.save(Task.builder().title("Fix login page bug")
					.description("Button is not working on mobile").assignedTo(ravi).build());

			taskRepository.save(Task.builder().title("Update documentation").description("Add API details")
					.assignedTo(sneha).build());
		}

		System.out.println("Sample data loaded successfully.");
	}

	private Role createRoleIfMissing(RoleName name) {
		return roleRepository.findByName(name).orElseGet(() -> {
			Role role = new Role();
			role.setName(name);
			return roleRepository.save(role);
		});
	}

	private User createUserIfMissing(String name, String email, String rawPassword, Set<Role> roles) {
		return userRepository.findByEmail(email).orElseGet(() -> userRepository.save(User.builder().name(name)
				.email(email).password(passwordEncoder.encode(rawPassword)).roles(new HashSet<>(roles)).build()));
	}
}