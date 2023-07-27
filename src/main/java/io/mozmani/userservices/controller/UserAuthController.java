package io.mozmani.userservices.controller;

import io.mozmani.userservices.domain.AuthResponsePacket;
import io.mozmani.userservices.domain.LoginRequest;
import io.mozmani.userservices.domain.RegisterRequest;
import io.mozmani.userservices.domain.UserDTO;
import io.mozmani.userservices.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller to handle user registration and logins.
 */
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;


    @PostMapping(value = "/login")
    public AuthResponsePacket postLogin(@RequestBody LoginRequest req) {
        return userAuthService.handleLogin(req);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequest req) {
        UserDTO user = userAuthService.registerUser(req);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
