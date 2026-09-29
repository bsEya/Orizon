package com.amaris.orizon.modules.gestionuser.repository;

import com.amaris.orizon.modules.gestionuser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<com.amaris.orizon.modules.gestionuser.entity.User, Long> {

    Optional<com.amaris.orizon.modules.gestionuser.entity.User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}