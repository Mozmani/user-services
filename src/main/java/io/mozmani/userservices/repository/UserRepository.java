package io.mozmani.userservices.repository;

import io.mozmani.userservices.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * Simple repository for User entities.
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    @Query(value = "SELECT COUNT(u) FROM UserEntity u WHERE LOWER(u.email) = LOWER(:email) " +
            "OR LOWER(u.username) = LOWER(:username)")
    Long countExistingUserRef(@Param("email") String email, @Param("username") String username);

}
