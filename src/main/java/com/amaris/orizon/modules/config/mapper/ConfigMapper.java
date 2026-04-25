package com.amaris.orizon.modules.config.mapper;

import com.amaris.orizon.modules.config.dto.FieldDefinitionDto;
import com.amaris.orizon.modules.config.dto.FieldOptionDto;
import com.amaris.orizon.modules.config.entity.FieldDefinition;
import com.amaris.orizon.modules.config.entity.FieldOption;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfigMapper {

    // --- Vers le Front (Entity -> DTO) ---
    FieldDefinitionDto toDto(FieldDefinition entity);

    FieldOptionDto toOptionDto(FieldOption entity);

    List<FieldDefinitionDto> toDtoList(List<FieldDefinition> entities);

    // --- Vers la Base (DTO -> Entity) ---

    @InheritInverseConfiguration
    FieldDefinition toEntity(FieldDefinitionDto dto);

    @InheritInverseConfiguration
    FieldOption toOptionEntity(FieldOptionDto dto);
}