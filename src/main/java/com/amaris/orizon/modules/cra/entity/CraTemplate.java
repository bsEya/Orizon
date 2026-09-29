package com.amaris.orizon.modules.cra.entity;

import com.amaris.orizon.modules.referentiel.entity.LP;
import com.amaris.orizon.modules.referentiel.entity.Perimetre;
import com.amaris.orizon.modules.referentiel.entity.Phase;
import com.amaris.orizon.modules.referentiel.entity.Project;
import com.amaris.orizon.modules.referentiel.entity.Squad;
import com.amaris.orizon.modules.gestionuser.entity.User;
import com.amaris.orizon.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Modèle d'activité sauvegardé par un utilisateur pour être réutilisé.
 * Correspond au bouton "Sauvegarder comme modèle" dans l'interface.
 */
@Entity
@Table(name = "cra_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CraTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom du modèle donné par l'utilisateur */
    @Column(nullable = false)
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squad_id")
    private Squad squad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perimetre_id")
    private Perimetre perimetre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id")
    private Phase phase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lp_id")
    private LP lp;

    private String description;
}
