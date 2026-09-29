package com.amaris.orizon.modules.gestionuser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * NOTE : structure déduite de UserAccessInitializer (user.getRole(), user.getRoles()).
 * À adapter/fusionner avec ta véritable entité User existante si les noms de champs diffèrent.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    /**
     * Optionnel : l'authentification est déléguée à Keycloak (OAuth2 Resource Server).
     * Ce champ n'est plus utilisé pour vérifier un mot de passe ; conservé seulement si
     * une compatibilité avec un ancien mécanisme d'auth locale est encore nécessaire.
     */
    @Column(nullable = true)
    private String password;

    @Column(unique = true)
    private String email;

    /**
     * ANCIEN champ (String libre) utilisé avant la migration vers le système Role/Permission.
     * À supprimer une fois UserAccessInitializer exécuté en production et validé.
     */
    @Column(name = "legacy_role")
    private String role;

    /**
     * NOUVEAU système : un utilisateur peut avoir plusieurs rôles.
     * fetch = EAGER ici par simplicité (peu de rôles par utilisateur en général) ;
     * passer en LAZY + @Transactional côté service si le volume grossit.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}