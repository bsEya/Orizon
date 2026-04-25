package com.amaris.orizon.modules.cra.entity;


import com.amaris.orizon.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cra_activity_types")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // ex: PROJECT_A, TRAINING, LEAVE

    @Column(nullable = false)
    private String label; // ex: "Projet Client A", "Formation", "Congés"

    private boolean billable; // Est-ce une activité facturable ?

    @Builder.Default
    private boolean active = true; // Pour désactiver une activité sans supprimer l'historique
}