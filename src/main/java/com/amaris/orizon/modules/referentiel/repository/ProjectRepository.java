package com.amaris.orizon.modules.referentiel.repository;

import com.amaris.orizon.modules.referentiel.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByActiveTrueOrderByLabelAsc();
    boolean existsByCode(String code);
}
