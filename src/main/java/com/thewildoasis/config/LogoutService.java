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

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final ITokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String token = jwtService.extractTokenFromHeader(request);
        List<Token> tokens = tokenRepository.findByToken(token);
        Iterable<Integer> ids = tokens
                .stream()
                .mapToInt(Token::getId)
                .boxed().toList();
        tokenRepository.deleteAllByIdInBatch(ids);
    }
}
