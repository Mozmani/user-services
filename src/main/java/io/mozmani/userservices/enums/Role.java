package io.mozmani.userservices.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Roles that exist in the current system, this is used as a simplified reference system to
 * prevent the need for additional table join calls.
 */
public enum Role {

    ADMIN_SUPER_USER("90667212-e148-4c41-af7c-b8000d7c6327", "Admin Super User"),
    ADMIN_CONTRIBUTOR("73ee4807-218a-4c48-9077-465378250a3c", "Admin Contributor"),
    EMPLOYEE_USER("c3774644-6ed2-4850-bd28-965828d2310a", "Employee User"),
    CUSTOMER_USER("c411e9b2-1280-49e1-82f3-b30b76021555", "Customer User");

    private static final Map<UUID, Role> BY_ID = new HashMap<>();
    private static final Map<String, Role> BY_READABLE_LABEL = new HashMap<>();
    private static final Map<String, Role> BY_NAME = new HashMap<>();

    // Simple one-time cache performant reads.
    static {
        for (Role r : values()) {
            BY_ID.put(r.id, r);
            BY_READABLE_LABEL.put(r.readableLabel, r);
            BY_NAME.put(r.name(), r);
        }
    }

    public final UUID id;
    public final String readableLabel;

    Role(String id, String readableLabel) {
        this.id = UUID.fromString(id);
        this.readableLabel = readableLabel;
    }

    /**
     * Simple grab id from enum.
     * @return UUID for role.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Simple grab readable label from enum.
     * @return readable label.
     */
    public String getReadableLabel() {
        return readableLabel;
    }

    public static Role getRoleByName(String roleName) {
        return BY_NAME.get(roleName);
    }

    public static boolean roleExists(String roleName) {
        return BY_NAME.get(roleName) != null;
    }

    public static UUID getRoleIdFromName(String roleName) {
        if (!roleExists(roleName)) {
            return null;
        }
        return BY_NAME.get(roleName).getId();
    }

    public static String getRoleNameFromId(UUID id) {
        Role role = BY_ID.get(id);
        if (null == role) {
            return null;
        }
        return role.name();
    }
}
