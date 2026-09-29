package com.amaris.orizon.modules.gestionuser.controller;

import com.amaris.orizon.exception.ResourceNotFoundException;
import com.amaris.orizon.modules.gestionuser.dto.CreatePermissionRequest;
import com.amaris.orizon.modules.gestionuser.dto.PermissionDto;
import com.amaris.orizon.modules.gestionuser.entity.Permission;
import com.amaris.orizon.modules.gestionuser.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionRepository permissionRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<PermissionDto> listPermissions() {
        return permissionRepository.findAll().stream().map(PermissionDto::fromEntity).toList();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<PermissionDto> createPermission(@RequestBody CreatePermissionRequest request) {
        if (permissionRepository.findByName(request.name()).isPresent()) {
            throw new IllegalArgumentException("La permission '" + request.name() + "' existe déjà");
        }
        Permission permission = permissionRepository.save(new Permission(request.name(), request.description()));
        return ResponseEntity.status(HttpStatus.CREATED).body(PermissionDto.fromEntity(permission));
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<Void> deletePermission(@PathVariable String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Permission introuvable : " + name));
        permissionRepository.delete(permission);
        return ResponseEntity.noContent().build();
    }
}