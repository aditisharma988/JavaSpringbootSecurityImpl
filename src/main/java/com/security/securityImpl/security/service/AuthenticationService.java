package com.security.securityImpl.security.service;

import com.security.securityImpl.security.dto.AuthResponse;

public interface AuthenticationService {


    AuthResponse generateAuthenticationResponse(final String username, final AuthResponse authResponse);

    AuthResponse generateAuthenticationResponseForPhoneNumber(final String phoneNumber, final AuthResponse authResponse);

}

