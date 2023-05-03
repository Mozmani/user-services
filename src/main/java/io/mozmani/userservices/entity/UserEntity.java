package io.mozmani.userservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entity for the users table.
 */

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    @Email(message = "Should be a valid email", regexp = ".+@.+\\..+")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_on", nullable = false)
    private OffsetDateTime createdOn;

    @Column(name = "updated_on", nullable = false)
    private OffsetDateTime updatedOn;

    @Column(name = "last_login")
    private OffsetDateTime lastLogin;

    @OneToMany(mappedBy = "user")
    private List<UserRoleEntity> userRoleEntities;


}
