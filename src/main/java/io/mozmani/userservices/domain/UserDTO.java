package io.mozmani.userservices.domain;

import io.mozmani.userservices.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String username;
    private String email;
    private List<String> roles;

    public UserDTO(UserEntity user, List<String> roles) {
        this.userId = String.valueOf(user.getId());
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = roles;
    }
}
