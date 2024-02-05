package com.thewildoasis.modules.authentication.service;

import com.thewildoasis.entities.User;
import com.thewildoasis.exception.GlobalAppException;
import com.thewildoasis.helpers.AuthHelper;
import com.thewildoasis.helpers.JwtService;
import com.thewildoasis.modules.authentication.dto.AuthenticationRequest;
import com.thewildoasis.modules.authentication.dto.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final AuthHelper authHelper;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest,
                                               HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication =
                UsernamePasswordAuthenticationToken.unauthenticated(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        UsernamePasswordAuthenticationToken authenticationResponse =
                (UsernamePasswordAuthenticationToken) authenticationManager.authenticate(authentication);
        authenticationResponse.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Important to save in context in latest version of spring security (requireExplicitSave is true by default may be can change via configuration)
        // Check https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#how-it-works-requireexplicitsave for more info
        final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authenticationResponse);
        securityContextHolderStrategy.setContext(context);
        return authHelper.getAuthenticationResponse(request, response, (User) authenticationResponse.getPrincipal());
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) throws GlobalAppException, AccessDeniedException {
        if (refreshToken == null) {
            throw new AccessDeniedException("Request Forbidden");
        }
        // Extract username claim from token. Throws exception if invalid/expired token is given in the request.
        String usernameInToken = jwtService.extractUsername(refreshToken);
        User user = (User) userDetailsService.loadUserByUsername(usernameInToken);
        // Check if token is valid
        boolean isTokenValid = jwtService.isTokenValid(refreshToken, user.getUsername());
        if (!isTokenValid) {
            throw new AccessDeniedException("Refresh Token Expired or invalid refresh Token received!");
        }
        return authHelper.getAuthenticationResponse(request, response, user);
    }

}


