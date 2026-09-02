package com.amaris.orizon.modules.cra.service;

import com.amaris.orizon.modules.cra.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface CraService {

    // --- Journée complète ---

    /** Récupère toutes les activités d'un utilisateur pour une journée donnée */
    DayResponse getDayActivities(Long userId, LocalDate date);

    /**
     * Enregistre toute la journée (bouton "Enregistrer ma journée").
     * Remplace atomiquement toutes les activités existantes pour cet utilisateur/date.
     */
    DayResponse saveDay(SaveDayRequest request);

    /**
     * Supprime toutes les activités d'une journée (bouton "Vider").
     */
    void clearDay(Long userId, LocalDate date);

    // --- Activité individuelle ---

    /** Crée une activité unique */
    CraEntryResponse createActivity(CraEntryRequest request);

    /** Récupère une activité par son ID */
    CraEntryResponse getActivityById(Long id);

    /** Met à jour une activité */
    CraEntryResponse updateActivity(Long id, CraEntryRequest request);

    /** Supprime une activité */
    void deleteActivity(Long id);

    /** Duplique une activité existante (même données, même journée) */
    CraEntryResponse duplicateActivity(Long id);

    // --- Historique ---

    /** Historique complet d'un utilisateur */
    List<CraEntryResponse> getUserHistory(Long userId);

    /** Historique filtré par période */
    List<CraEntryResponse> getUserHistoryForPeriod(Long userId, LocalDate startDate, LocalDate endDate);
}