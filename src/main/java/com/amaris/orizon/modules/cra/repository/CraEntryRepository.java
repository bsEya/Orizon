package com.amaris.orizon.modules.cra.repository;

import com.amaris.orizon.modules.cra.entity.CraEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CraEntryRepository extends JpaRepository<CraEntry, Long> {

    List<CraEntry> findByUserUserIdOrderByDateDesc(Long userId);

    List<CraEntry> findByUserUserIdAndDateOrderByCreatedAtAsc(Long userId, LocalDate date);

    List<CraEntry> findByUserUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate startDate, LocalDate endDate);

    /** Somme des heures pour un utilisateur sur une journée (pour validation max 8h) */
    @Query("SELECT COALESCE(SUM(c.hours), 0.0) FROM CraEntry c WHERE c.user.userId = :userId AND c.date = :date")
    Double findTotalHoursByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    /** Même somme en excluant une entrée (pour la mise à jour) */
    @Query("SELECT COALESCE(SUM(c.hours), 0.0) FROM CraEntry c WHERE c.user.userId = :userId AND c.date = :date AND c.id <> :entryId")
    Double findTotalHoursByUserIdAndDateExcludingEntry(@Param("userId") Long userId, @Param("date") LocalDate date, @Param("entryId") Long entryId);

    /** Suppression de toutes les activités d'une journée (bouton "Vider") */
    @Modifying
    @Query("DELETE FROM CraEntry c WHERE c.user.userId = :userId AND c.date = :date")
    void deleteByUserUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}