package com.amaris.orizon.modules.referentiel.repository;

import com.amaris.orizon.modules.referentiel.entity.Squad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Long> {
    List<Squad> findByActiveTrueOrderByLabelAsc();
    boolean existsByCode(String code);
}
