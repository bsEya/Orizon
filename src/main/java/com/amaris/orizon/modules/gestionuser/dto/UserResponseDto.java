package com.amaris.orizon.modules.gestionuser.dto;

import lombok.Data;
import java.util.Map;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Map<String, String> attributes;
}