package com.amaris.orizon.modules.gestionuser.dto;

import com.amaris.orizon.modules.gestionuser.entity.User;

import java.util.List;

public record UserDto(Long id, String username, String email, List<String> roles) {

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(r -> r.getName()).sorted().toList()
        );
    }
}