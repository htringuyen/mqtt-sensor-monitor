package org.iotwarehouse.monitorapp.domain;

public record ObserverAuth(String observerName, String observerPassword, String roleName, String rolePassword) {

    public ObserverAuth {

        if (observerName == null || observerName.isBlank()) {
            throw new IllegalArgumentException("Observer name cannot be null or empty");
        }
        if (observerPassword == null || observerPassword.isBlank()) {
            throw new IllegalArgumentException("Observer password cannot be null or empty");
        }
        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        if (rolePassword == null || rolePassword.isBlank()) {
            throw new IllegalArgumentException("Role password cannot be null or empty");
        }

    }
}
