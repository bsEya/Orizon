package com.amaris.orizon.modules.cra.dto;

import com.amaris.orizon.modules.referentiel.dto.ReferentielItemDto;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour un modèle d'activité sauvegardé.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraTemplateResponse {
    private Long id;
    private String label;
    private Long userId;
    private ReferentielItemDto activityType;
    private ReferentielItemDto project;
    private ReferentielItemDto squad;
    private ReferentielItemDto perimetre;
    private ReferentielItemDto phase;
    private ReferentielItemDto lp;
    private String description;
    private LocalDateTime createdAt;
}
