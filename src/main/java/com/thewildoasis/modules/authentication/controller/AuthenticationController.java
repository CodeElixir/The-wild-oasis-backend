package com.thewildoasis.modules.authentication.controller;

import com.thewildoasis.modules.authentication.dto.AuthenticationRequest;
import com.thewildoasis.modules.authentication.dto.AuthenticationResponse;
import com.thewildoasis.modules.authentication.service.IAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Getter
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest authenticationRequest,
                                                        HttpServletRequest request, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = getAuthenticationService().authenticate(authenticationRequest, request, response);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response,
                                                               @CookieValue(value = "${application.security.jwt.refresh-token.cookie-name}") String refreshToken) throws AccessDeniedException {
        AuthenticationResponse authenticationResponse = getAuthenticationService().refreshToken(request, response, refreshToken);
        return ResponseEntity.ok(authenticationResponse);
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
