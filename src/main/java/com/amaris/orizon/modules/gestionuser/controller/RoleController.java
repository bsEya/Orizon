package com.amaris.orizon.modules.gestionuser.controller;

import com.amaris.orizon.exception.ResourceNotFoundException;
import com.amaris.orizon.modules.gestionuser.dto.CreateRoleRequest;
import com.amaris.orizon.modules.gestionuser.dto.RoleDto;
import com.amaris.orizon.modules.gestionuser.entity.Permission;
import com.amaris.orizon.modules.gestionuser.entity.Role;
import com.amaris.orizon.modules.gestionuser.repository.PermissionRepository;
import com.amaris.orizon.modules.gestionuser.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestion des rôles. Tout est protégé par la permission ROLE_MANAGE (voir PermissionInitializer) :
 * c'est l'exemple concret de gestion "dynamique" des droits évoquée depuis le début de la conversation.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()") // lecture ouverte à tout utilisateur connecté
    public List<RoleDto> listRoles() {
        return roleRepository.findAll().stream().map(RoleDto::fromEntity).toList();
    }

    @GetMapping("/{name}")
    @PreAuthorize("isAuthenticated()")
    public RoleDto getRole(@PathVariable String name) {
        return RoleDto.fromEntity(findRoleOrThrow(name));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RoleDto> createRole(@RequestBody CreateRoleRequest request) {
        if (roleRepository.findByName(request.name()).isPresent()) {
            throw new IllegalArgumentException("Le rôle '" + request.name() + "' existe déjà");
        }
        Role role = roleRepository.save(new Role(request.name()));
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleDto.fromEntity(role));
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<Void> deleteRole(@PathVariable String name) {
        Role role = findRoleOrThrow(name);
        roleRepository.delete(role);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roleName}/permissions/{permissionName}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RoleDto> addPermission(@PathVariable String roleName,
                                                 @PathVariable String permissionName) {
        Role role = findRoleOrThrow(roleName);
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new ResourceNotFoundException("Permission introuvable : " + permissionName));

        role.addPermission(permission);
        roleRepository.save(role);
        return ResponseEntity.ok(RoleDto.fromEntity(role));
    }

    @DeleteMapping("/{roleName}/permissions/{permissionName}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RoleDto> removePermission(@PathVariable String roleName,
                                                    @PathVariable String permissionName) {
        Role role = findRoleOrThrow(roleName);
        role.getPermissions().removeIf(p -> p.getName().equals(permissionName));
        roleRepository.save(role);
        return ResponseEntity.ok(RoleDto.fromEntity(role));
    }

    private Role findRoleOrThrow(String name) {
        return roleRepository.findWithPermissionsByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable : " + name));
    }
}