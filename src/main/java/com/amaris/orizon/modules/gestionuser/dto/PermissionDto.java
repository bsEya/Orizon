package com.amaris.orizon.modules.gestionuser.dto;

import com.amaris.orizon.modules.gestionuser.entity.Permission;

public record PermissionDto(Long id, String name, String description) {

    public static PermissionDto fromEntity(Permission permission) {
        return new PermissionDto(permission.getId(), permission.getName(), permission.getDescription());
    }
}
