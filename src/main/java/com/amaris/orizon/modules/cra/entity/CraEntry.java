package com.amaris.orizon.modules.cra.entity;

import com.amaris.orizon.modules.referentiel.entity.LP;
import com.amaris.orizon.modules.referentiel.entity.Perimetre;
import com.amaris.orizon.modules.referentiel.entity.Phase;
import com.amaris.orizon.modules.referentiel.entity.Project;
import com.amaris.orizon.modules.referentiel.entity.Squad;
import com.amaris.orizon.modules.user.entity.User;
import com.amaris.orizon.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cra_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CraEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Date de la journée imputée */
    @Column(nullable = false)
    private LocalDate date;

    /** Durée en heures réelles (ex: 2.5, 4.0). Max 8h/jour au total. */
    @Column(nullable = false)
    private Double hours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Type d'activité : Mission / Travail / Congé / Férié */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_id", nullable = false)
    private ActivityType activityType;

    /** Projet (optionnel pour Congé/Férié) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    /** Squad */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squad_id")
    private Squad squad;

    /** Périmètre */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perimetre_id")
    private Perimetre perimetre;

    /** Phase */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id")
    private Phase phase;

    /** LP (Lot de Programme) — optionnel */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lp_id")
    private LP lp;

    /** Description libre de l'activité */
    private String description;
}