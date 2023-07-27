package io.mozmani.userservices.service;

import io.mozmani.userservices.domain.AuthResponsePacket;
import io.mozmani.userservices.domain.LoginRequest;
import io.mozmani.userservices.domain.RegisterRequest;
import io.mozmani.userservices.domain.UserDTO;
import io.mozmani.userservices.entity.UserEntity;
import io.mozmani.userservices.security.JWTUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Authentication and authorization service for user management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthService {

    @Value("${custom-security.private-key}")
    File privateKey;
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userReferenceService.processUserLoginStamp(user);
        responsePacket.setAuthorized(true);
        responsePacket.setSystemMessage("Login attempt was successful");
        return responsePacket;
    }

    public UserDTO registerUser(RegisterRequest req) {
        boolean newUser = userReferenceService.existingUserCheck(req.getEmail(), req.getUsername());
        if (!newUser || !passwordIsValid(req.getPassword())) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPassword(BCrypt.hashpw(req.getPassword(), BCrypt.gensalt()));
        return userReferenceService.processNewUser(user, req.getRoles());
    }

    /**
     * Placeholder for password requirements.
     * @param password a password.
     * @return value if password meets criteria.
     */
    private boolean passwordIsValid(String password) {
        return password.length() > 3;
    }
}
