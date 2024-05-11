package org.iotwarehouse.monitorapp.service;

import org.iotwarehouse.monitorapp.domain.ObserverAuth;

public class AuthenticationServiceImpl implements AuthenticationService {

    AuthenticationServiceImpl() {

    }

    @Override
    public ObserverAuth authenticate(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (username.equals("observer") && password.equals("observer")) {
            return new ObserverAuth("observer", "observer", "role", "role");
        }

        return null;
    }
}
