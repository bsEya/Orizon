package com.amaris.orizon.modules.cra.dto;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO de requête pour créer ou mettre à jour une activité CRA.
 * Utilise des IDs pour référencer les entités du référentiel.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraEntryRequest {

    private LocalDate date;

    /** Heures imputées (ex: 2.5, 4.0). Max total 8h/jour. */
    private Double hours;

    private Long userId;

    /** ID du type d'activité (Mission=1, Travail=2, Congé=3, Férié=4) */
    private Long activityTypeId;

    /** ID du projet (optionnel) */
    private Long projectId;

    /** ID de la squad (optionnel) */
    private Long squadId;

    /** ID du périmètre (optionnel) */
    private Long perimetreId;

    /** ID de la phase (optionnel) */
    private Long phaseId;

    /** ID du LP / Lot de Programme (optionnel) */
    private Long lpId;

    private String description;
}
