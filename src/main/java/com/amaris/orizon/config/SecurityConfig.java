package com.amaris.orizon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Désactivation de CSRF (Cross-Site Request Forgery)
            // C'est souvent nécessaire pour les API REST stateless, surtout si vous utilisez des tokens (comme JWT).
            // Si vous utilisez des sessions traditionnelles (cookies), vous devriez l'activer.
            .csrf(AbstractHttpConfigurer::disable)
            
            // Intégration de la configuration CORS définie dans WebConfig.java
            .cors(Customizer.withDefaults())
            
            // Configuration des autorisations pour les différentes routes
            .authorizeHttpRequests(authz -> authz
                // --- Routes publiques (accessibles sans authentification) ---
                
                // Swagger UI et OpenAPI docs
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Endpoints d'authentification / inscription (à créer ultérieurement)
                // .requestMatchers("/api/v1/auth/**").permitAll()
                
                // Temporaire: permettre l'accès à l'API config et cra sans authentification
                // TODO: A enlever une fois l'authentification mise en place avec des utilisateurs réels
                .requestMatchers("/api/v1/config/**", "/api/v1/cra/**").permitAll()
                
                // --- Routes protégées ---
                // Toutes les autres requêtes nécessitent d'être authentifié
                .anyRequest().authenticated()
            )
            
            // Configuration par défaut pour l'authentification HTTP Basic (pour tester rapidement)
            // Pour un vrai projet, on utilisera plutôt un filtre JWT (JSON Web Token)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Le PasswordEncoder est essentiel pour ne pas stocker les mots de passe en clair dans la base de données.
    // BCrypt est l'algorithme standard recommandé.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
