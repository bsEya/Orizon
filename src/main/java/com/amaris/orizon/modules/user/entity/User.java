package com.amaris.orizon.modules.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "orizon_users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Column(unique = true, nullable = false)
    private String username;

    @Email(message = "Email invalide")
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String role; // ex: TECH_LEAD, CONSULTANT, MANAGER

    // Audit simple sans passer par une classe parente pour ce premier test
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Approche paramétrable : stocker des paramètres spécifiques
     * (ex: quota d'heures, matricule, spécialité BPMN) sous forme de clé/valeur.
     */
    @ElementCollection
    @CollectionTable(name = "user_attributes", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    private Map<String, String> attributes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}