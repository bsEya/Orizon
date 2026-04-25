package com.amaris.orizon.modules.config.repository;

import com.amaris.orizon.modules.config.entity.FieldDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldDefinitionRepository extends JpaRepository<FieldDefinition, Long> {

    /**
     * Récupère la liste des champs paramétrables pour un module et une équipe donnés.
     */
    List<FieldDefinition> findByModuleCodeAndTeamCode(String moduleCode, String teamCode);
}