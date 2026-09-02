package com.amaris.orizon.modules.cra.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO pour enregistrer toute une journée d'imputations en une seule requête.
 * Correspond au bouton "Enregistrer ma journée".
 * Remplace toutes les activités existantes pour cet utilisateur à cette date.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveDayRequest {

    private Long userId;
    private LocalDate date;

    /** Liste complète des activités de la journée */
    private List<CraEntryRequest> activities;
}
