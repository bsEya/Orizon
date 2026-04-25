package com.amaris.orizon.modules.cra.repository;

import com.amaris.orizon.modules.cra.entity.CraEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CraEntryRepository extends JpaRepository<CraEntry, Long> {

    List<CraEntry> findByUserId(Long userId);
    List<CraEntry> findByUserIdOrderByDateDesc(Long userId);

    List<CraEntry> findByUserIdAndDate(Long userId, LocalDate date);
    List<CraEntry> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(c.duration), 0.0) FROM CraEntry c WHERE c.userId = :userId AND c.date = :date")
    Double findTotalDurationByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(c.duration), 0.0) FROM CraEntry c WHERE c.userId = :userId AND c.date = :date AND c.id <> :entryId")
    Double findTotalDurationByUserIdAndDateExcludingEntry(@Param("userId") Long userId, @Param("date") LocalDate date, @Param("entryId") Long entryId);
}