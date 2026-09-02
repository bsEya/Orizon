package com.amaris.orizon.modules.referentiel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO générique pour tous les référentiels (Projet, Squad, Périmètre, Phase, LP, ActivityType).
 * Retourné par les endpoints GET /api/v1/ref/*
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferentielItemDto {
    private Long id;
    private String code;
    private String label;
    private boolean active;
}
