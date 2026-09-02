package com.amaris.orizon.modules.cra.repository;

import com.amaris.orizon.modules.cra.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {}

