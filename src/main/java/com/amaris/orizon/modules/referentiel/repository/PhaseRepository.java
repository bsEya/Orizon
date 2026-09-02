package com.amaris.orizon.modules.referentiel.repository;

import com.amaris.orizon.modules.referentiel.entity.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {
    List<Phase> findByActiveTrueOrderByLabelAsc();
    boolean existsByCode(String code);
}
