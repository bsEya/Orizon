package com.amaris.orizon.modules.cra.controller;

import com.amaris.orizon.modules.cra.entity.CraEntry;
import com.amaris.orizon.modules.cra.service.CraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cra")
@RequiredArgsConstructor
@Tag(name = "CRA Management", description = "Endpoints pour la gestion des imputations de temps")
public class CraController {

    private final CraService craService;

    @PostMapping("/impute")
    @Operation(summary = "Enregistrer une nouvelle imputation",
            description = "Permet de saisir du temps pour un utilisateur. Vérifie que la durée journalière ne dépasse pas 1.0 et valide les champs dynamiques de l'équipe.")
    @ApiResponse(responseCode = "201", description = "Imputation enregistrée avec succès")
    @ApiResponse(responseCode = "400", description = "Erreur de validation (ex: dépassement du quota ou champ paramétrable manquant)")
    public ResponseEntity<CraEntry> createImputation(
            @RequestBody CraEntry entry,
            @RequestParam String team) {
        CraEntry savedEntry = craService.saveImputation(entry, team);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une imputation par son ID")
    @ApiResponse(responseCode = "200", description = "Imputation trouvée")
    @ApiResponse(responseCode = "404", description = "Imputation non trouvée")
    public ResponseEntity<CraEntry> getImputationById(@PathVariable Long id) {
        return ResponseEntity.ok(craService.getImputationById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Récupérer l'historique d'un utilisateur pour une période donnée",
            description = "Récupère toutes les imputations pour un utilisateur. Si startDate et endDate sont fournis, filtre les résultats pour cette période.")
    public ResponseEntity<List<CraEntry>> getUserHistory(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "Date de début (format YYYY-MM-DD)") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "Date de fin (format YYYY-MM-DD)") LocalDate endDate) {

        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(craService.getUserHistoryForPeriod(userId, startDate, endDate));
        }
        return ResponseEntity.ok(craService.getUserHistory(userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une imputation existante",
            description = "Permet de modifier une imputation de temps. Ré-applique les validations.")
    @ApiResponse(responseCode = "200", description = "Imputation mise à jour avec succès")
    @ApiResponse(responseCode = "400", description = "Erreur de validation")
    @ApiResponse(responseCode = "404", description = "Imputation non trouvée")
    public ResponseEntity<CraEntry> updateImputation(
            @PathVariable Long id,
            @RequestBody CraEntry entry,
            @RequestParam String team) {
        return ResponseEntity.ok(craService.updateImputation(id, entry, team));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une imputation")
    @ApiResponse(responseCode = "204", description = "Imputation supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Imputation non trouvée")
    public ResponseEntity<Void> deleteImputation(@PathVariable Long id) {
        craService.deleteImputation(id);
        return ResponseEntity.noContent().build();
    }
}