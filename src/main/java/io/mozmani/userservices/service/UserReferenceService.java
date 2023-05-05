package io.mozmani.userservices.service;

import io.mozmani.userservices.entity.UserEntity;
import io.mozmani.userservices.entity.UserRoleEntity;
import io.mozmani.userservices.enums.Role;
import io.mozmani.userservices.repository.UserRepository;
import io.mozmani.userservices.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

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
}
