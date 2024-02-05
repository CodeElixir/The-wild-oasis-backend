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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}
