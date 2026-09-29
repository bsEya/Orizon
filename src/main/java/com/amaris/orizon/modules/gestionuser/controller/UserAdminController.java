package com.amaris.orizon.modules.gestionuser.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserAdminController {

    // Bonne pratique RBAC dynamique : on vérifie la PERMISSION, pas le nom du rôle.
    // Un admin pourra retirer USER_DELETE du rôle MANAGER sans toucher au code.
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public void deleteUser(@PathVariable Long id) {
        // ...
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public void updateUser(@PathVariable Long id) {
        // ...
    }

    // hasRole reste utile pour des vérifications grossières (accès à toute une section admin)
    @GetMapping("/admin-panel")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel() {
        return "panel admin";
    }
}