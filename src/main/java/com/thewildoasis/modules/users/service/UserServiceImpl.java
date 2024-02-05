package com.thewildoasis.modules.users.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Token;
import com.thewildoasis.entities.User;
import com.thewildoasis.exception.UserAlreadyExistException;
import com.thewildoasis.exception.UserNotFoundException;
import com.thewildoasis.helpers.AuthHelper;
import com.thewildoasis.helpers.JwtService;
import com.thewildoasis.modules.authentication.dto.AuthenticationResponse;
import com.thewildoasis.modules.users.dto.ChangePasswordRequest;
import com.thewildoasis.modules.users.dto.UpdateUser;
import com.thewildoasis.modules.users.repository.IUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthHelper authHelper;
    private final JwtService jwtService;

    @Override
    public User getUser(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
    }

    @Override
    public AuthenticationResponse register(User user, HttpServletRequest request, HttpServletResponse response) {
        Token token = authHelper.getToken(user);
        Set<Token> tokens = Set.of(token);
        user.setTokens(tokens);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already registered with given email id.");
        }
        User savedUser = userRepository.save(user);
        authHelper.setRefreshCookie(request, response, jwtService.generateRefreshToken(user));
        return AuthenticationResponse.builder()
                .user(savedUser)
                .accessToken(token.getToken())
                .build();
    }

    @Override
    public User changePassword(ChangePasswordRequest passwordRequest, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        user.setConfirmPassword(passwordRequest.getConfirmationPassword());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, UpdateUser updateUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
        user.setFullName(updateUser.getFullName());
        user.setAvatar(updateUser.getAvatar());
        user.setConfirmPassword(user.getPassword());
        return userRepository.save(user);
    }

}
