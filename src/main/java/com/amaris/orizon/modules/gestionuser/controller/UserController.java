package com.amaris.orizon.modules.gestionuser.controller;

import com.amaris.orizon.modules.gestionuser.entity.User;
import com.amaris.orizon.modules.gestionuser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Gestion des utilisateurs et de leurs attributs")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Créer un nouvel utilisateur")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping //Lister tous les utilisateurs
    @Operation(summary = "Lister tous les utilisateurs")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}