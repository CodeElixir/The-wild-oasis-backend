package com.thewildoasis.filters;

import com.thewildoasis.entities.Token;
import com.thewildoasis.entities.User;
import com.thewildoasis.helpers.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Check if request contains authorization header. If not continue the request to next filter chain.
        final String token = jwtService.extractTokenFromHeader(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // Extract username claim from token. Throws exception if invalid/expired token is given in the request.
            String usernameInToken = jwtService.extractUsername(token);
            // If username exists, token is valid and Security Context has no Authentication i.e, not authenticated,
            // then validate token through DB and set Security Context to tell framework that the request is authenticated.
            // If validation fails, Security Context is null and request is forbidden.
            if (usernameInToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = (User) this.userDetailsService.loadUserByUsername(usernameInToken);
                // Fetch all the tokens assigned to user and filter out current token from the request.
                Optional<Token> userToken = user.getTokens().stream().filter(t -> t.getToken().equals(token)).findFirst();
                // Check if token is valid
                boolean isTokenValid = userToken.map(t -> !t.getExpired() && !t.getRevoked()).orElse(false);
                // Check if token is expired
                boolean isTokenExpired = jwtService.isTokenExpired(token);
                if (isTokenValid & !isTokenExpired) {
                    final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
                    final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext context = securityContextHolderStrategy.createEmptyContext();
                    context.setAuthentication(authentication);
                    securityContextHolderStrategy.setContext(context);
                }
            }
        } catch (Exception e) {
            throw new AccessDeniedException("Token Expired or invalid Token received!");
        }
        filterChain.doFilter(request, response);
    }

    // Don't apply this filter on public paths
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("/api/v1/auth") ||
                request.getServletPath().contains("/api/v1/ping");
    }
}
