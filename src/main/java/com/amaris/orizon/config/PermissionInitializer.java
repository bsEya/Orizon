package com.amaris.orizon.config;


import com.amaris.orizon.modules.gestionuser.entity.Permission;
import com.amaris.orizon.modules.gestionuser.entity.Role;
import com.amaris.orizon.modules.gestionuser.repository.PermissionRepository;
import com.amaris.orizon.modules.gestionuser.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Seed initial des permissions et de leur association aux rôles (idempotent : ne recrée rien
 * si l'entrée existe déjà). Doit s'exécuter AVANT UserAccessInitializer, d'où @Order(1).
 *
 * À terme, une fois l'admin capable de créer rôles/permissions via une UI dédiée,
 * ce runner peut être limité au profil "dev"/"local" ou supprimé.
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class PermissionInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    private static final Map<String, List<String>> ROLE_PERMISSIONS = Map.of(
            "ADMIN", List.of("USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DELETE", "ROLE_MANAGE"),
            "MANAGER", List.of("USER_READ", "USER_UPDATE"),
            "USER", List.of("USER_READ")
    );

    @Override
    @Transactional
    public void run(String... args) {
        ROLE_PERMISSIONS.forEach((roleName, permissionNames) -> {
            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));

            for (String permName : permissionNames) {
                Permission permission = permissionRepository.findByName(permName)
                        .orElseGet(() -> permissionRepository.save(new Permission(permName, null)));

                role.addPermission(permission);
            }
            roleRepository.save(role);
        });
    }
}