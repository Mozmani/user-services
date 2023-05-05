package io.mozmani.userservices.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.mozmani.userservices.entity.UserEntity;
import io.mozmani.userservices.service.UserReferenceService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthFilter extends OncePerRequestFilter {

    @Value("${custom-security.public-key}")
    String publicKey;

    private final UserReferenceService userReferenceService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/auth/**")) {
            filterChain.doFilter(request, response);
        }

        String authHeader = request.getHeader(AUTHORIZATION);
        if (null != authHeader && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring("Bearer ".length());
                DecodedJWT validToken = JWTUtility.verifyUserJWT(token, publicKey);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Make Token
                    UUID userId = UUID.fromString(validToken.getClaim("userID").asString());
                    UserEntity user = userReferenceService.findUserById(userId);
                    UserAuthenticationToken authenticationToken = new UserAuthenticationToken(
                      userReferenceService.findUserAuthorities(user), userId);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                log.error("There has been an issue authorizing this request", e);
                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    SecurityContextHolder.getContext().setAuthentication(null);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
