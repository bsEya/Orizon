package com.amaris.orizon.modules.cra.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de réponse pour une journée complète d'imputations.
 * Inclut la liste des activités et le total des heures.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayResponse {

    private LocalDate date;
    private Long userId;

    /** Liste des activités de la journée */
    private List<CraEntryResponse> activities;

    /** Total des heures imputées pour la journée (calculé côté serveur) */
    private Double totalHours;

    /** Nombre d'heures restantes avant le plafond de 8h */
    private Double remainingHours;
}
