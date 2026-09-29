package com.amaris.orizon.modules.gestionuser.dto;


import com.amaris.orizon.modules.gestionuser.entity.Role;

import java.util.List;

public record RoleDto(Long id, String name, List<String> permissions) {

    public static RoleDto fromEntity(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName(),
                role.getPermissions().stream().map(p -> p.getName()).sorted().toList()
        );
    }
}