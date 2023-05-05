package io.mozmani.userservices.repository;

import io.mozmani.userservices.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Simple repository for User entities.
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
