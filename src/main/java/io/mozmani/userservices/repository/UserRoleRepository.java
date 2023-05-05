package io.mozmani.userservices.repository;

import io.mozmani.userservices.entity.UserEntity;
import io.mozmani.userservices.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Simple repository for user roles.
 */
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UUID> {
    List<UserRoleEntity> findAllByUser(UserEntity user);
}
