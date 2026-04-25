package com.amaris.orizon.modules.cra.service;

import com.amaris.orizon.modules.cra.entity.CraEntry;

import java.time.LocalDate;
import java.util.List;

public interface CraService {

    /**
     * Enregistre une nouvelle imputation après validation.
     * @param entry L'imputation à enregistrer.
     * @param teamCode Le code de l'équipe pour valider les champs dynamiques.
     * @return L'entité CraEntry sauvegardée.
     */
    CraEntry saveImputation(CraEntry entry, String teamCode);

    /**
     * Récupère une imputation par son identifiant unique.
     * @param id L'ID de l'imputation.
     * @return L'entité CraEntry trouvée.
     * @throws com.amaris.orizon.modules.cra.exception.ResourceNotFoundException si l'imputation n'est pas trouvée.
     */
    CraEntry getImputationById(Long id);

    /**
     * Récupère tout l'historique des imputations pour un utilisateur.
     * @param userId L'ID de l'utilisateur.
     * @return Une liste de CraEntry.
     */
    List<CraEntry> getUserHistory(Long userId);

    /**
     * Récupère l'historique des imputations pour un utilisateur sur une période donnée.
     * @param userId L'ID de l'utilisateur.
     * @param startDate La date de début.
     * @param endDate La date de fin.
     * @return Une liste de CraEntry filtrée par date.
     */
    List<CraEntry> getUserHistoryForPeriod(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Met à jour une imputation existante.
     * @param id L'ID de l'imputation à mettre à jour.
     * @param entryDetails Les nouvelles données de l'imputation.
     * @param teamCode Le code de l'équipe pour la re-validation.
     * @return L'entité CraEntry mise à jour.
     */
    CraEntry updateImputation(Long id, CraEntry entryDetails, String teamCode);

    /**
     * Supprime une imputation par son identifiant.
     * @param id L'ID de l'imputation à supprimer.
     */
    void deleteImputation(Long id);
}