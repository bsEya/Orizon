package com.amaris.orizon.modules.cra.dto;

import com.amaris.orizon.modules.referentiel.dto.ReferentielItemDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de réponse pour une activité CRA.
 * Inclut les objets référentiels complets (id + code + label) pour l'affichage.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraEntryResponse {

    private Long id;
    private LocalDate date;
    private Double hours;

    private Long userId;
    private String userName;

    private ReferentielItemDto activityType;
    private ReferentielItemDto project;
    private ReferentielItemDto squad;
    private ReferentielItemDto perimetre;
    private ReferentielItemDto phase;
    private ReferentielItemDto lp;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
