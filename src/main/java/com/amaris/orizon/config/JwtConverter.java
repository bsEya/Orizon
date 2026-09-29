package com.amaris.orizon.config;

import com.amaris.orizon.modules.gestionuser.entity.User;
import com.amaris.orizon.modules.gestionuser.repository.RoleRepository;
import com.amaris.orizon.modules.gestionuser.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Version fusionnée : le comportement d'origine (extraction des rôles Keycloak depuis
 * realm_access/resource_access) est INCHANGÉ, pour ne rien casser des modules existants
 * (ex: cra) qui pourraient déjà s'appuyer sur des autorités "ROLE_user"/"ROLE_admin".
 *
 * AJOUT : lecture des Role/Permission de NOTRE base locale (orizon_core), avec provisioning
 * automatique de l'utilisateur au premier appel. C'est ce qui permet à /api/roles,
 * /api/permissions et /api/users (protégés par hasAuthority('ROLE_MANAGE') etc.) de
 * fonctionner avec des droits gérés dynamiquement, indépendamment de la config Keycloak.
 */
@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    public JwtConverter(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional // nécessaire : role.getPermissions() est LAZY, il faut une session ouverte pendant convert()
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {

        Collection<GrantedAuthority> authorities = Stream.of(
                        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractRoles(jwt).stream(),
                        extractLocalAuthorities(jwt).stream()
                )
                .flatMap(s -> s)
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPrincipleClaimName(jwt)
        );
    }

    /**
     * this method extract the userId from the JWT
     * @param jwt represents the token
     * @return the userId
     */
    private String getPrincipleClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (principleAttribute != null && jwt.hasClaim(principleAttribute)) {
            claimName = principleAttribute;
        }
        return jwt.getClaim(claimName);
    }

    /**
     * Extracts roles from both realm_access and resource_access claims.
     * INCHANGÉ par rapport à l'original.
     */
    private Collection<? extends GrantedAuthority> extractRoles(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        // Realm roles (ex: "user", "admin")
        Object realmAccessObj = jwt.getClaim("realm_access");
        if (realmAccessObj instanceof Map<?, ?> realmAccess) {
            Object realmRolesObj = realmAccess.get("roles");
            if (realmRolesObj instanceof Collection<?> realmRoles) {
                realmRoles.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .forEach(roles::add);
            }
        }

        // Client / Resource roles
        Object resourceAccessObj = jwt.getClaim("resource_access");
        if (resourceAccessObj instanceof Map<?, ?> resourceAccess && resourceId != null) {
            Object resourceObj = resourceAccess.get(resourceId);
            if (resourceObj instanceof Map<?, ?> resource) {
                Object clientRolesObj = resource.get("roles");
                if (clientRolesObj instanceof Collection<?> clientRoles) {
                    clientRoles.stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .forEach(roles::add);
                }
            }
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    /**
     * NOUVEAU : va chercher (ou crée) l'utilisateur dans NOTRE base (orizon_core), puis
     * transforme ses Role/Permission locaux en GrantedAuthority. C'est cette méthode qui
     * alimente hasAuthority('ROLE_MANAGE'), hasAuthority('USER_READ'), etc.
     */
    private Set<GrantedAuthority> extractLocalAuthorities(Jwt jwt) {
        String username = getPrincipleClaimName(jwt);
        if (username == null) {
            return Set.of();
        }

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> provisionUser(jwt, username));

        return user.getRoles().stream()
                .flatMap(role -> Stream.concat(
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getName())),
                        role.getPermissions().stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                ))
                .collect(Collectors.toSet());
    }

    /**
     * Provisioning "just-in-time" : premier appel d'un utilisateur authentifié par Keycloak
     * mais encore inconnu localement -> on lui crée une ligne avec le rôle USER par défaut.
     */
    private User provisionUser(Jwt jwt, String username) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(jwt.getClaimAsString("email"));

        roleRepository.findByName("USER").ifPresent(role -> user.getRoles().add(role));

        return userRepository.save(user);
    }
}