package com.amaris.orizon.modules.gestionuser.service;

import com.amaris.orizon.exception.BusinessException;
import com.amaris.orizon.modules.gestionuser.repository.UserRepository;
import com.amaris.orizon.modules.gestionuser.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j // Pour les logs de debug
@Service
@RequiredArgsConstructor // Injection propre par constructeur via Lombok
public class UserService {

    private final UserRepository userRepository;

    /**
     * Crée un utilisateur avec une vérification d'unicité.
     */
    @Transactional
    public User createUser(User user) {
        log.info("Tentative de création de l'utilisateur : {}", user.getUsername());

        // Vérification métier : unicité de l'email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("Un utilisateur avec cet email existe déjà.");
        }

        // Vérification métier : unicité du username
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BusinessException("Ce nom d'utilisateur est déjà pris.");
        }

        return userRepository.save(user);
    }

    /**
     * Récupère tous les utilisateurs
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Récupère un utilisateur par son ID ou lance une exception propre.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Utilisateur non trouvé avec l'id : " + id));
    }
}