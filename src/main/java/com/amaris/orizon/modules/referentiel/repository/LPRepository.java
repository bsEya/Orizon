package com.amaris.orizon.modules.referentiel.repository;

import com.amaris.orizon.modules.referentiel.entity.LP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LPRepository extends JpaRepository<LP, Long> {
    List<LP> findByActiveTrueOrderByLabelAsc();
    boolean existsByCode(String code);
}
