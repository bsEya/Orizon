package com.amaris.orizon.modules.config.controller;

import com.amaris.orizon.modules.config.dto.FieldDefinitionDto;
import com.amaris.orizon.modules.config.entity.FieldDefinition;
import com.amaris.orizon.modules.config.mapper.ConfigMapper;
import com.amaris.orizon.modules.config.service.FieldConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
@Tag(name = "Configuration API", description = "Gestion CRUD du paramétrage dynamique")
public class ConfigController {

    private final FieldConfigService fieldConfigService;
    private final ConfigMapper configMapper;

    // --- LECTURE ---

    @GetMapping("/fields/{module}/{team}")
    @Operation(summary = "Récupérer la configuration par module et équipe")
    public ResponseEntity<List<FieldDefinitionDto>> getFields(
            @PathVariable String module,
            @PathVariable String team) {
        List<FieldDefinition> entities = fieldConfigService.getFieldsConfiguration(module, team);
        return ResponseEntity.ok(configMapper.toDtoList(entities));
    }

    // --- CRÉATION ---

    @PostMapping("/fields")
    @Operation(summary = "Ajouter une nouvelle définition de champ")
    public ResponseEntity<FieldDefinitionDto> createField(@Valid @RequestBody FieldDefinitionDto dto) {
        FieldDefinition entity = configMapper.toEntity(dto);
        FieldDefinition saved = fieldConfigService.saveField(entity);
        return new ResponseEntity<>(configMapper.toDto(saved), HttpStatus.CREATED);
    }

    // --- MODIFICATION ---

    @PutMapping("/fields/{id}")
    @Operation(summary = "Modifier une définition de champ existante")
    public ResponseEntity<FieldDefinitionDto> updateField(
            @PathVariable Long id,
            @Valid @RequestBody FieldDefinitionDto dto) {

        FieldDefinition entity = configMapper.toEntity(dto);
        FieldDefinition updated = fieldConfigService.updateField(id, entity);
        return ResponseEntity.ok(configMapper.toDto(updated));
    }

    // --- SUPPRESSION ---

    @DeleteMapping("/fields/{id}")
    @Operation(summary = "Supprimer un champ et ses options associées")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        fieldConfigService.deleteField(id);
        return ResponseEntity.noContent().build();
    }
}