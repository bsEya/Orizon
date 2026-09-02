package com.amaris.orizon.modules.cra.repository;

import com.amaris.orizon.modules.cra.entity.CraTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CraTemplateRepository extends JpaRepository<CraTemplate, Long> {
    List<CraTemplate> findByUserUserIdOrderByLabelAsc(Long userId);
}
