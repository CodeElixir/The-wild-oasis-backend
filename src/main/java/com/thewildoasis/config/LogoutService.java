package com.thewildoasis.config;

import com.thewildoasis.entities.Token;
import com.thewildoasis.helpers.JwtService;
import com.thewildoasis.modules.tokens.repository.ITokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final ITokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String token = jwtService.extractTokenFromHeader(request);
        Optional<Token> optionalToken = tokenRepository.findByToken(token);
        if (token != null && optionalToken.isPresent()) {
            Token storedToken = optionalToken.get();
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
    }
}
