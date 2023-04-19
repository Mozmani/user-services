package io.mozmani.userservices.domain;

import lombok.Data;

/**
 * Simple inbound login request dto.
 */
@Data
public class LoginRequest {
    private String userRef;
    private String password;
}
