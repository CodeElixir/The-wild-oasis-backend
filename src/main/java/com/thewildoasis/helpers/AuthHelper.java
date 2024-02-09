package com.thewildoasis.helpers;

import com.thewildoasis.entities.Token;
import com.thewildoasis.entities.TokenType;
import com.thewildoasis.entities.User;
import com.thewildoasis.modules.authentication.dto.AuthenticationResponse;
import com.thewildoasis.modules.tokens.repository.ITokenRepository;
import com.thewildoasis.modules.users.repository.IUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class AuthHelper {

    private final IUserRepository userRepository;
    private final ITokenRepository tokenRepository;
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;

    public AuthenticationResponse getAuthenticationResponse(HttpServletRequest request, HttpServletResponse response, User user) {
        Token token = getToken(user);
        if (!user.getTokens().isEmpty()) {
            revokeAllUserTokens(user);
        }
        tokenRepository.save(token);
        User savedUser = userRepository.save(user);
        setRefreshCookie(request, response, jwtService.generateRefreshToken(user));
        return AuthenticationResponse.builder()
                .user(savedUser)
                .accessToken(token.getToken())
                .build();
    }

    private void revokeAllUserTokens(User user) {
        Iterable<Integer> ids = user.getTokens()
                .stream()
                .mapToInt(Token::getId)
                .boxed().toList();
        tokenRepository.deleteAllByIdInBatch(ids);
    }

    public Token getToken(User user) {
        return Token.builder()
                .user(user)
                .token(jwtService.generateToken(user))
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
    }

    public void setRefreshCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        ResponseCookie responseCookie = ResponseCookie
                .from(refreshTokenCookieName, refreshToken)
                .secure(request.isSecure())
                .path(getRequestContext(request))
                .maxAge(Duration.between(LocalDateTime.now(),
                        jwtService.extractExpiration(refreshToken).toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDateTime()))
                .httpOnly(true).build();
        Cookie cookie = mapToCookie(responseCookie);
        response.addCookie(cookie);
    }

    private String getRequestContext(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return (!contextPath.isEmpty()) ? contextPath : "/";
    }

    private Cookie mapToCookie(ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setSecure(responseCookie.isSecure());
        cookie.setPath(responseCookie.getPath());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        if (StringUtils.hasLength(responseCookie.getDomain())) {
            cookie.setDomain(responseCookie.getDomain());
        }
        if (StringUtils.hasText(responseCookie.getSameSite())) {
            cookie.setAttribute("SameSite", responseCookie.getSameSite());
        }
        return cookie;
    }
}
