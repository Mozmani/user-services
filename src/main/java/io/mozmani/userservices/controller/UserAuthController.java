package io.mozmani.userservices.controller;

import io.mozmani.userservices.domain.AuthResponsePacket;
import io.mozmani.userservices.domain.LoginRequest;
import io.mozmani.userservices.service.UserAuthService;
import lombok.RequiredArgsConstructor;
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
}
