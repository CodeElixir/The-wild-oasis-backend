package com.thewildoasis.modules.authentication.service;

import com.thewildoasis.modules.authentication.dto.AuthenticationRequest;
import com.thewildoasis.modules.authentication.dto.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.file.AccessDeniedException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest, HttpServletRequest request, HttpServletResponse response);

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) throws AccessDeniedException;

}
