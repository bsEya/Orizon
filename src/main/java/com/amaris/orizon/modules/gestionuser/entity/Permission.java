package com.amaris.orizon.modules.gestionuser.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente une permission unitaire et granulaire (ex: "USER_READ", "USER_DELETE", "ROLE_MANAGE").
 * Une permission n'a de sens que via son association à un ou plusieurs Role.
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // equals/hashCode basés sur l'id : indispensable pour un comportement correct
    // dans les Set<Permission> une fois l'entité persistée (id non-null).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
