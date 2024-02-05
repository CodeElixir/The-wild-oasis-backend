package com.thewildoasis.modules.users.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Token;
import com.thewildoasis.entities.User;
import com.thewildoasis.exception.GlobalAppException;
import com.thewildoasis.exception.ResourceNotFoundException;
import com.thewildoasis.exception.UserAlreadyExistException;
import com.thewildoasis.exception.UserNotFoundException;
import com.thewildoasis.helpers.AuthHelper;
import com.thewildoasis.helpers.JwtService;
import com.thewildoasis.modules.authentication.dto.AuthenticationResponse;
import com.thewildoasis.modules.users.dto.ChangePasswordRequest;
import com.thewildoasis.modules.users.dto.UpdateUser;
import com.thewildoasis.modules.users.repository.IUserRepository;
import com.thewildoasis.s3.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthHelper authHelper;
    private final JwtService jwtService;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String s3Bucket;

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
    public User updateUser(Integer id, UpdateUser updateUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
        user.setFullName(updateUser.getFullName());
        user.setConfirmPassword(user.getPassword());
        return userRepository.save(user);
    }

    @Override
    public User changePassword(ChangePasswordRequest passwordRequest, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        user.setConfirmPassword(passwordRequest.getConfirmationPassword());
        return userRepository.save(user);
    }

    @Override
    public ResponseDto uploadAvatar(Integer id, MultipartFile file) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User doesn't exist to upload avatar"));
        String userAvatarID = user.getAvatar() == null || user.getAvatar().isEmpty() ?
                UUID.randomUUID().toString() : user.getAvatar();
        try {
            s3Service.putObject(s3Bucket,
                    "user-avatars/%s/%s".formatted(id, userAvatarID),
                    file.getBytes());
        } catch (Exception e) {
            throw new GlobalAppException("Failed to upload avatar to s3", e);
        }
        user.setAvatar(userAvatarID);
        userRepository.save(user);
        return new ResponseDto(1, "User avatar uploaded successfully");
    }

    @Override
    public byte[] getAvatar(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User doesn't exist to download avatar"));
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            throw new ResourceNotFoundException(
                    "User with id [%s] has no avatar".formatted(id));
        }
        return s3Service.getObject(s3Bucket, "user-avatars/%s/%s".formatted(id, user.getAvatar()));
    }

}
