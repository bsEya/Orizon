package com.amaris.orizon.modules.cra.dto;

import com.amaris.orizon.modules.referentiel.dto.ReferentielItemDto;
import lombok.*;

/**
 * DTO de requête pour sauvegarder un modèle d'activité.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraTemplateSaveRequest {
    /** Nom donné au modèle par l'utilisateur */
    private String label;
    private Long userId;
    private Long activityTypeId;
    private Long projectId;
    private Long squadId;
    private Long perimetreId;
    private Long phaseId;
    private Long lpId;
    private String description;
}
