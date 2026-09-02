package com.amaris.orizon.modules.cra.controller;

import com.amaris.orizon.modules.cra.dto.CraTemplateResponse;
import com.amaris.orizon.modules.cra.dto.CraTemplateSaveRequest;
import com.amaris.orizon.modules.cra.service.CraTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cra/templates")
@RequiredArgsConstructor
@Tag(name = "CRA - Modèles", description = "Gestion des modèles d'activité sauvegardés par l'utilisateur")
public class CraTemplateController {

    private final CraTemplateService templateService;

    @GetMapping
    @Operation(
        summary = "Lister les modèles d'un utilisateur",
        description = "Retourne tous les modèles d'activité sauvegardés par un utilisateur, triés par nom."
    )
    public ResponseEntity<List<CraTemplateResponse>> getUserTemplates(@RequestParam Long userId) {
        return ResponseEntity.ok(templateService.getUserTemplates(userId));
    }

    @PostMapping
    @Operation(
        summary = "Sauvegarder un modèle d'activité",
        description = "Enregistre la configuration d'une activité comme modèle réutilisable (bouton 'Sauvegarder comme modèle')."
    )
    @ApiResponse(responseCode = "201", description = "Modèle créé")
    public ResponseEntity<CraTemplateResponse> saveTemplate(@RequestBody CraTemplateSaveRequest request) {
        return new ResponseEntity<>(templateService.saveTemplate(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un modèle")
    @ApiResponse(responseCode = "204", description = "Modèle supprimé")
    @ApiResponse(responseCode = "404", description = "Modèle introuvable")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
