package com.example.flyway.constants.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ERole {
    ADMIN,
    MANAGER,
    GUEST,
    STAFF;

    public static String getRoleHierarchy() {
        return Arrays.stream(ERole.values())
            .map(ERole::getRole)
            .collect(Collectors.joining(" > "));
    }

    public static String getCustomHierarchy() {
        return Arrays.stream(ERole.values())
            .map(ERole::getRole)
            .collect(Collectors.joining(">"));
    }

    public String getRole() {
        return name();
    }
}