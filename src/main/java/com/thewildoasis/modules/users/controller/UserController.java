package com.thewildoasis.modules.users.controller;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.User;
import com.thewildoasis.modules.authentication.dto.AuthenticationResponse;
import com.thewildoasis.modules.users.dto.ChangePasswordRequest;
import com.thewildoasis.modules.users.dto.UpdateUser;
import com.thewildoasis.modules.users.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Data
@Validated
public class UserController {
    private final IUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(getUserService().getUser(id));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody User user,
                                                           HttpServletRequest request, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = getUserService().register(user, request, response);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(getUserService().updateUser(id, updateUser));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<User> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) {
        return ResponseEntity.ok(getUserService().changePassword(changePasswordRequest, principal));
    }

    @PostMapping(value = "/uploadAvatar/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> uploadAvatar(@PathVariable Integer id,
                                                    @RequestBody MultipartFile file) {
        return ResponseEntity.ok(userService.uploadAvatar(id, file));
    }

    @GetMapping(value = "{id}/avatar",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getUserAvatar(@PathVariable("id") Integer id) {
        return userService.getAvatar(id);
    }
}
