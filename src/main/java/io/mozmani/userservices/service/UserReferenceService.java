package io.mozmani.userservices.service;

import io.mozmani.userservices.domain.UserDTO;
import io.mozmani.userservices.entity.UserEntity;
import io.mozmani.userservices.entity.UserRoleEntity;
import io.mozmani.userservices.enums.Role;
import io.mozmani.userservices.repository.UserRepository;
import io.mozmani.userservices.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Simple service to access user details.
 */
@Service
@RequiredArgsConstructor
public class UserReferenceService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserEntity findUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Set<SimpleGrantedAuthority> findUserAuthorities(UserEntity user) {
        List<UserRoleEntity> userRoles = userRoleRepository.findAllByUser(user);
        if (null == userRoles || userRoles.isEmpty()) {
            return Collections.emptySet();
        }
        return userRoles.stream().map(
                uRole -> new SimpleGrantedAuthority(Role.getRoleNameFromId(uRole.getRoleId())))
                .collect(Collectors.toSet());
    }

    public UserEntity findUserByReference(String userRef) {
        UserEntity user = userRepository.findByEmail(userRef);
        if (null == user) {
            user = userRepository.findByUsername(userRef);
            if (null == user) {
                throw new IllegalArgumentException("Incorrect username or password provided.");
            }
        }
        return user;
    }

    public boolean existingUserCheck(String email, String username) {
        Long existingUsers = userRepository.countExistingUserRef(email, username);
        return existingUsers.equals(0L);
    }

    @Transactional
    public UserDTO processNewUser(UserEntity user, List<String> roles) {
        OffsetDateTime now = OffsetDateTime.now();
        user.setCreatedOn(now);
        user.setUpdatedOn(now);
        userRepository.saveAndFlush(user);
        if (null != roles && !roles.isEmpty()) {
            for (String role : roles) {
                UserRoleEntity userRole = new UserRoleEntity();
                userRole.setUser(user);
                userRole.setRoleId(Role.getRoleIdFromName(role));
                userRoleRepository.save(userRole);
            }
        }
        return new UserDTO(user, roles);
    }

    public void processUserLoginStamp(UserEntity user) {
        user.setLastLogin(OffsetDateTime.now());
        userRepository.save(user);
    }
}
