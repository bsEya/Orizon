package com.amaris.orizon.modules.cra.controller;

import com.amaris.orizon.modules.cra.dto.*;
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
@Tag(name = "CRA - Imputations", description = "Gestion des imputations de temps (activités par journée)")
public class CraController {

    private final CraService craService;

    // ============================================================
    // JOURNÉE COMPLÈTE
    // ============================================================

    @GetMapping("/day")
    @Operation(
        summary = "Récupérer toutes les activités d'une journée",
        description = "Retourne la liste des activités imputées par un utilisateur pour une date donnée, avec le total et le restant des heures."
    )
    @ApiResponse(responseCode = "200", description = "Journée récupérée")
    public ResponseEntity<DayResponse> getDayActivities(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Date au format YYYY-MM-DD") LocalDate date) {
        return ResponseEntity.ok(craService.getDayActivities(userId, date));
    }

    @PostMapping("/day")
    @Operation(
        summary = "Enregistrer toute la journée",
        description = "Sauvegarde l'ensemble des activités d'une journée en une seule requête (bouton 'Enregistrer ma journée'). Remplace les activités existantes pour cet utilisateur/date."
    )
    @ApiResponse(responseCode = "200", description = "Journée enregistrée avec succès")
    @ApiResponse(responseCode = "400", description = "Validation échouée (total > 8h, champ manquant...)")
    public ResponseEntity<DayResponse> saveDay(@RequestBody SaveDayRequest request) {
        return ResponseEntity.ok(craService.saveDay(request));
    }

    @DeleteMapping("/day")
    @Operation(
        summary = "Vider toutes les activités d'une journée",
        description = "Supprime toutes les activités imputées pour un utilisateur à une date donnée (bouton 'Vider')."
    )
    @ApiResponse(responseCode = "204", description = "Journée vidée")
    public ResponseEntity<Void> clearDay(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        craService.clearDay(userId, date);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // ACTIVITÉ INDIVIDUELLE
    // ============================================================

    @PostMapping("/activities")
    @Operation(
        summary = "Créer une activité",
        description = "Ajoute une ligne d'activité pour une journée. Valide que le total journalier ne dépasse pas 8h."
    )
    @ApiResponse(responseCode = "201", description = "Activité créée")
    @ApiResponse(responseCode = "400", description = "Validation échouée")
    public ResponseEntity<CraEntryResponse> createActivity(@RequestBody CraEntryRequest request) {
        return new ResponseEntity<>(craService.createActivity(request), HttpStatus.CREATED);
    }

    @GetMapping("/activities/{id}")
    @Operation(summary = "Récupérer une activité par son ID")
    @ApiResponse(responseCode = "200", description = "Activité trouvée")
    @ApiResponse(responseCode = "404", description = "Activité introuvable")
    public ResponseEntity<CraEntryResponse> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(craService.getActivityById(id));
    }

    @PutMapping("/activities/{id}")
    @Operation(
        summary = "Mettre à jour une activité",
        description = "Modifie une activité existante. Ré-applique toutes les validations (max 8h/jour)."
    )
    @ApiResponse(responseCode = "200", description = "Activité mise à jour")
    @ApiResponse(responseCode = "400", description = "Validation échouée")
    @ApiResponse(responseCode = "404", description = "Activité introuvable")
    public ResponseEntity<CraEntryResponse> updateActivity(
            @PathVariable Long id,
            @RequestBody CraEntryRequest request) {
        return ResponseEntity.ok(craService.updateActivity(id, request));
    }

    @DeleteMapping("/activities/{id}")
    @Operation(summary = "Supprimer une activité")
    @ApiResponse(responseCode = "204", description = "Activité supprimée")
    @ApiResponse(responseCode = "404", description = "Activité introuvable")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        craService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/activities/{id}/duplicate")
    @Operation(
        summary = "Dupliquer une activité",
        description = "Crée une copie exacte d'une activité existante (même journée, mêmes données). Bouton 'Dupliquer'."
    )
    @ApiResponse(responseCode = "201", description = "Activité dupliquée")
    @ApiResponse(responseCode = "400", description = "Duplication impossible (dépasserait 8h)")
    @ApiResponse(responseCode = "404", description = "Activité source introuvable")
    public ResponseEntity<CraEntryResponse> duplicateActivity(@PathVariable Long id) {
        return new ResponseEntity<>(craService.duplicateActivity(id), HttpStatus.CREATED);
    }

    // ============================================================
    // HISTORIQUE
    // ============================================================

    @GetMapping("/history/{userId}")
    @Operation(
        summary = "Historique des imputations d'un utilisateur",
        description = "Retourne toutes les activités d'un utilisateur, filtrables par période."
    )
    public ResponseEntity<List<CraEntryResponse>> getUserHistory(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Date de début YYYY-MM-DD") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Date de fin YYYY-MM-DD") LocalDate endDate) {

        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(craService.getUserHistoryForPeriod(userId, startDate, endDate));
        }
        return ResponseEntity.ok(craService.getUserHistory(userId));
    }
}
