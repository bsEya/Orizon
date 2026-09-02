package com.amaris.orizon.modules.referentiel.repository;

import com.amaris.orizon.modules.referentiel.entity.Perimetre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerimetreRepository extends JpaRepository<Perimetre, Long> {
    List<Perimetre> findByActiveTrueOrderByLabelAsc();
    boolean existsByCode(String code);
}
