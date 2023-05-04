package io.mozmani.userservices.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Simple spring security token for users to be cached in the security context. This provides a reference for
 * authenticated users, as well as a simple way to determine a reference of the current interacting user.
 */
public class UserAuthenticationToken extends AbstractAuthenticationToken {

    private final Collection<SimpleGrantedAuthority> authorities;
    private final Set<String> roles;
    private final String email;
    private final UUID userId;


    /**
     * Creates a user token for the spring security context.
     * @param authorities This collection acts as the list of roles for the user.
     * @param email the principle user's email.
     * @param userId The principle user's primary id.
     */
    public UserAuthenticationToken(Collection<SimpleGrantedAuthority> authorities, String email, UUID userId) {
        super(authorities);
        this.authorities = authorities;
        this.roles = authorities.stream().map(String::valueOf).collect(Collectors.toSet());
        this.email = email;
        this.userId = userId;
        this.setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return "credentials not cached in the system.";
    }

    @Override
    public UUID getPrincipal() {
        return userId;
    }
}
