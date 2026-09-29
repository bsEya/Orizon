package com.amaris.orizon.config;


import com.amaris.orizon.modules.gestionuser.entity.Role;
import com.amaris.orizon.modules.gestionuser.repository.RoleRepository;
import com.amaris.orizon.modules.gestionuser.repository.UserRepository;
import com.amaris.orizon.modules.gestionuser.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserAccessInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        for (User user : userRepository.findAll()) {
            String oldRole = user.getRole();

            if (oldRole == null || oldRole.isBlank()) {
                continue;
            }

            String roleName = oldRole.trim();
            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(roleName);
                        return roleRepository.save(newRole);
                    });

            if (user.getRoles().add(role)) {
                userRepository.save(user);
            }
        }
    }
}