package com.amaris.orizon.modules.referentiel.controller;

import com.amaris.orizon.modules.cra.entity.ActivityType;
import com.amaris.orizon.modules.cra.repository.ActivityTypeRepository;
import com.amaris.orizon.modules.referentiel.dto.ReferentielItemDto;
import com.amaris.orizon.modules.referentiel.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ref")
@RequiredArgsConstructor
@Tag(name = "Référentiels", description = "Données de référence pour alimenter les listes déroulantes du CRA")
public class ReferentielController {

    private final ActivityTypeRepository activityTypeRepository;
    private final ProjectRepository projectRepository;
    private final SquadRepository squadRepository;
    private final PerimetreRepository perimetreRepository;
    private final PhaseRepository phaseRepository;
    private final LPRepository lpRepository;

    @GetMapping("/activity-types")
    @Operation(
        summary = "Liste des types d'activité",
        description = "Retourne les types : Mission, Travail, Congé, Férié"
    )
    public ResponseEntity<List<ReferentielItemDto>> getActivityTypes() {
        List<ReferentielItemDto> result = activityTypeRepository.findAll().stream()
                .filter(ActivityType::isActive)
                .map(a -> ReferentielItemDto.builder()
                        .id(a.getId()).code(a.getCode()).label(a.getLabel()).active(a.isActive()).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/projects")
    @Operation(summary = "Liste des projets", description = "Ex: Bootcamp, Camunda Autosense, Orizon...")
    public ResponseEntity<List<ReferentielItemDto>> getProjects() {
        return ResponseEntity.ok(
            projectRepository.findByActiveTrueOrderByLabelAsc().stream()
                .map(p -> ReferentielItemDto.builder().id(p.getId()).code(p.getCode()).label(p.getLabel()).active(p.isActive()).build())
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/squads")
    @Operation(summary = "Liste des squads", description = "Ex: Squad1, Squad2, Squad3, Transverses...")
    public ResponseEntity<List<ReferentielItemDto>> getSquads() {
        return ResponseEntity.ok(
            squadRepository.findByActiveTrueOrderByLabelAsc().stream()
                .map(s -> ReferentielItemDto.builder().id(s.getId()).code(s.getCode()).label(s.getLabel()).active(s.isActive()).build())
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/perimetres")
    @Operation(summary = "Liste des périmètres", description = "Ex: Dev, Sprint Daily, Sprint planning, Tests B2B...")
    public ResponseEntity<List<ReferentielItemDto>> getPerimetres() {
        return ResponseEntity.ok(
            perimetreRepository.findByActiveTrueOrderByLabelAsc().stream()
                .map(p -> ReferentielItemDto.builder().id(p.getId()).code(p.getCode()).label(p.getLabel()).active(p.isActive()).build())
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/phases")
    @Operation(summary = "Liste des phases", description = "Ex: Autorisation, Autosense, BI, Documentation, Formation...")
    public ResponseEntity<List<ReferentielItemDto>> getPhases() {
        return ResponseEntity.ok(
            phaseRepository.findByActiveTrueOrderByLabelAsc().stream()
                .map(p -> ReferentielItemDto.builder().id(p.getId()).code(p.getCode()).label(p.getLabel()).active(p.isActive()).build())
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/lps")
    @Operation(summary = "Liste des LPs (Lots de Programme)", description = "Ex: 68.2, 68.5, VDR, RENO, Orizon...")
    public ResponseEntity<List<ReferentielItemDto>> getLPs() {
        return ResponseEntity.ok(
            lpRepository.findByActiveTrueOrderByLabelAsc().stream()
                .map(l -> ReferentielItemDto.builder().id(l.getId()).code(l.getCode()).label(l.getLabel()).active(l.isActive()).build())
                .collect(Collectors.toList())
        );
    }
}
