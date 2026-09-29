package com.amaris.orizon.modules.gestionuser.service;


import com.amaris.orizon.modules.gestionuser.entity.User;
import com.amaris.orizon.modules.gestionuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Point central du RBAC dynamique : au moment de l'authentification, on charge
 * l'utilisateur puis on "aplatit" Role -> Permission en une liste de GrantedAuthority.
 *
 * Deux familles d'autorités sont générées :
 *  - "ROLE_XXX"  -> pour compatibilité avec hasRole('XXX') / @Secured
 *  - "PERMISSION_NAME" (ex: "USER_DELETE") -> pour hasAuthority('USER_DELETE'), la bonne pratique en RBAC
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true) // garde la session Hibernate ouverte pour les collections LAZY (permissions)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> Stream.concat(
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getName())),
                        role.getPermissions().stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                ))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
//                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}