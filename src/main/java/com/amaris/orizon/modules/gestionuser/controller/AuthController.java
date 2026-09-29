package com.amaris.orizon.modules.gestionuser.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Il n'y a plus de /login ni /register ici : c'est Keycloak qui authentifie
 * (endpoint token de Keycloak, voir TESTING.md). Ce contrôleur ne fait
 * qu'exposer ce que l'API voit une fois le token validé, pratique pour déboguer
 * un problème d'autorisation ("pourquoi je suis en 403 ?").
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
        List<String> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .map(Object::toString)
                .toList();

        return Map.of(
                "username", jwt.getClaimAsString("preferred_username"),
                "email", jwt.getClaimAsString("email"),
                "tokenIssuer", jwt.getIssuer().toString(),
                "authoritiesLocales", authorities
        );
    }
}