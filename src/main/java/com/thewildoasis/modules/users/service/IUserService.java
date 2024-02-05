package com.thewildoasis.modules.users.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.User;
import com.thewildoasis.modules.authentication.dto.AuthenticationResponse;
import com.thewildoasis.modules.users.dto.ChangePasswordRequest;
import com.thewildoasis.modules.users.dto.UpdateUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface IUserService {

    User getUser(Integer id);

    AuthenticationResponse register(User user, HttpServletRequest request, HttpServletResponse response);

    User updateUser(Integer id, UpdateUser updateUser);

    User changePassword(ChangePasswordRequest passwordRequest, Principal principal);

    ResponseDto uploadAvatar(Integer id, MultipartFile file);

    byte[] getAvatar(Integer id);
}
