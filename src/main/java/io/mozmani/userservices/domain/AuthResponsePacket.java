package io.mozmani.userservices.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple response packet for authentication requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponsePacket {
    private String requestType;
    private String token;
    private String systemMessage;
    private boolean isAuthorized;
}
