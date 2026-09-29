package com.amaris.orizon.modules.gestionuser.repository;

import com.amaris.orizon.modules.gestionuser.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    /**
     * Variante avec chargement forcé des permissions (JOIN FETCH via EntityGraph).
     * À utiliser quand on sait qu'on va lire role.getPermissions() en dehors
     * d'une transaction active, pour éviter une LazyInitializationException.
     */
    @EntityGraph(attributePaths = "permissions")
    Optional<Role> findWithPermissionsByName(String name);
}