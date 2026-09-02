package com.amaris.orizon.modules.config.dto;

import java.util.List;

public record FieldDefinitionDto(
        Long id,               // Ajout de l'ID (très important pour les modifications/retours)
        String moduleCode,     // Ajout du moduleCode
        String teamCode,
        String fieldKey,
        String fieldLabel,
        String type,
        boolean required,
        List<FieldOptionDto> options
) {}
