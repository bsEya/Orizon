package com.amaris.orizon.modules.user.repository;

import com.amaris.orizon.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Une méthode pratique pour ton futur login ou tes recherches
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}