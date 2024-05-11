package org.iotwarehouse.monitorapp.service;

import org.iotwarehouse.monitorapp.domain.ObserverAuth;
import org.iotwarehouse.monitorapp.service.exception.AuthenticationException;

public interface AuthenticationService {

    public ObserverAuth authenticate(String username, String password) throws AuthenticationException;
}
