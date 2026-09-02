package com.amaris.orizon.modules.config.service;

import com.amaris.orizon.common.exception.ResourceNotFoundException;
import com.amaris.orizon.modules.config.entity.FieldDefinition;
import com.amaris.orizon.modules.config.repository.FieldDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.amaris.orizon.modules.cra.service.CraService;
import org.springframework.context.annotation.Lazy;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class FieldConfigService {

    private final FieldDefinitionRepository fieldDefinitionRepository;

    @Transactional(readOnly = true)
    public List<FieldDefinition> getFieldsConfiguration(String module, String team) {
        // Grâce au FetchType.EAGER ou à une jointure, les options seront chargées
        return fieldDefinitionRepository.findByModuleCodeAndTeamCode(module, team);
    }
    public FieldConfigService(FieldDefinitionRepository fieldDefinitionRepository) {
        this.fieldDefinitionRepository = fieldDefinitionRepository;
    }

    @Transactional
    public FieldDefinition saveField(FieldDefinition field) {
        // S'assurer que le lien entre le champ et ses options est correct pour JPA
        if (field.getOptions() != null) {
            field.getOptions().forEach(opt -> opt.setFieldDefinition(field));
        }
        return fieldDefinitionRepository.save(field);
    }

    @Transactional
    public FieldDefinition updateField(Long id, FieldDefinition newDetails) {
        return fieldDefinitionRepository.findById(id)
                .map(field -> {
                    field.setFieldLabel(newDetails.getFieldLabel());
                    field.setRequired(newDetails.isRequired());
                    field.setType(newDetails.getType()); // N'oublie pas le type

                    // Si tu reçois de nouvelles options
                    if (newDetails.getOptions() != null) {
                        // On vide et on remplace pour gérer l'orphanRemoval
                        field.getOptions().clear();
                        newDetails.getOptions().forEach(opt -> {
                            opt.setFieldDefinition(field);
                            field.getOptions().add(opt);
                        });
                    }

                    return fieldDefinitionRepository.save(field);
                }).orElseThrow(() -> new ResourceNotFoundException("Field not found with id " + id));
    }

    @Transactional
    public void deleteField(Long id) {
        if (!fieldDefinitionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : le champ avec l'id " + id + " n'existe pas.");
        }
        fieldDefinitionRepository.deleteById(id);
    }

}