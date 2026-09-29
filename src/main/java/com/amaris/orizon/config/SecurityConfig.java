package com.amaris.orizon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This config class is used to configure the security layer of the application.
 * It disables CSRF protection,requires authentication for all requests,
 * and configures the application to use JWTs for OAuth2 resource server support.
 * Sessions are managed in a stateless manner, meaning no session state is maintained between requests.
 * This setup ensures that all incoming requests are authenticated using JWT tokens.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // AJOUTÉ : sans cette ligne, @PreAuthorize("hasAuthority(...)") est ignoré silencieusement
public class SecurityConfig {
    private final JwtConverter jwtConverter;

    public SecurityConfig(JwtConverter jwtConverter) {
        this.jwtConverter = jwtConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authz -> authz
                        // --- Routes publiques (accessibles sans authentification) ---
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/v1/ref/**").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()

                        // --- Routes protégées (CRA et autres) ---
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder(
            @org.springframework.beans.factory.annotation.Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:http://localhost:8081/realms/orizon/protocol/openid-connect/certs}") String jwkSetUri) {
        return org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}