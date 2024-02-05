package com.thewildoasis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewildoasis.dto.ErrorResponseDto;
import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.filters.CsrfCookieFilter;
import com.thewildoasis.filters.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**", "/api/v1/users/register"};

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CsrfCookieFilter csrfCookieFilter;
    private final LogoutService logoutService;
    private final ObjectMapper objectMapper;

    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;
    @Value("#{'${cors.allowed-methods}'.split(',')}")
    private List<String> allowedMethods;
    @Value("#{'${cors.allowed-headers}'.split(',')}")
    private List<String> allowedHeaders;
    @Value("#{'${cors.exposed-headers}'.split(',')}")
    private List<String> exposedHeaders;

    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfigurer -> csrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
//                .addFilterAfter(csrfCookieFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowCredentials(true);
//                    configuration.setAllowedOrigins(allowedOrigins);
                    configuration.setAllowedMethods(allowedMethods);
                    configuration.setAllowedHeaders(allowedHeaders);
                    configuration.setExposedHeaders(exposedHeaders);
                    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);
                    return configuration;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) ->
                        requests.requestMatchers(WHITE_LIST_URL).permitAll().anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                            ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                    request.getServletPath(),
                                    HttpStatus.FORBIDDEN,
                                    accessDeniedException.getMessage(),
                                    LocalDateTime.now()
                            );
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            objectMapper.writeValue(response.getOutputStream(), errorResponseDto);
                        }).authenticationEntryPoint((request, response, authException) -> {
                            ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                    request.getServletPath(),
                                    HttpStatus.FORBIDDEN,
                                    authException.getMessage(),
                                    LocalDateTime.now()
                            );
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            objectMapper.writeValue(response.getOutputStream(), errorResponseDto);
                        }))
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutService)
                        .deleteCookies(refreshTokenCookieName)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            ResponseDto responseDto = new ResponseDto(1, "Logged out Successfully");
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_OK);
                            objectMapper.writeValue(response.getOutputStream(), responseDto);
                        }));
//        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
