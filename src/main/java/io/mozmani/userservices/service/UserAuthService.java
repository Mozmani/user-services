package io.mozmani.userservices.service;

import io.mozmani.userservices.domain.AuthResponsePacket;
import io.mozmani.userservices.domain.LoginRequest;
import io.mozmani.userservices.entity.UserEntity;
import io.mozmani.userservices.security.JWTUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;

/**
 * Authentication and authorization service for user management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthService {

    @Value("${custom-security.private-key}")
    String privateKey;
    @Value("${custom-security.public-key}")
    String publicKey;

    private final UserReferenceService userReferenceService;

    public AuthResponsePacket handleLogin(LoginRequest req) {
        UserEntity user = userReferenceService.findUserByReference(req.getUserRef());
        boolean isValid = BCrypt.checkpw(req.getPassword(), user.getPassword());
        if (!isValid) {
            throw new IllegalArgumentException("Incorrect username or password provided.");
        }
        AuthResponsePacket responsePacket = new AuthResponsePacket();
        responsePacket.setRequestType("LOGIN_ATTEMPT");
        try {
            String token = JWTUtility.generateUserToken(privateKey, publicKey, String.valueOf(user.getId()));
            responsePacket.setToken(token);
        } catch (GeneralSecurityException e) {
            responsePacket.setAuthorized(false);
            responsePacket.setSystemMessage("Issue processing the login request.");
            log.error("Issue creating a toke for userRef: {}", req.getUserRef());
        }
        responsePacket.setAuthorized(true);
        responsePacket.setSystemMessage("Login attempt was successful");
        return responsePacket;
    }
}
