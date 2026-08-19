package com.amaris.orizon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class SecurityConfig {
    private final JwtConverter jwtConverter;

    public SecurityConfig(JwtConverter jwtConverter) {
        this.jwtConverter = jwtConverter;
    }

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

                    // --- Routes protégées ---
                    .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        (oauth2)-> oauth2.jwt(
                                jwt-> jwt.jwtAuthenticationConverter(jwtConverter)
                        ))
                .sessionManagement(
                        session-> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    // Le PasswordEncoder est essentiel pour ne pas stocker les mots de passe en clair dans la base de données.
    // BCrypt est l'algorithme standard recommandé.
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
