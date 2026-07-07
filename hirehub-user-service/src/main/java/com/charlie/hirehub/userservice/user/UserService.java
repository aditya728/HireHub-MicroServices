package com.charlie.hirehub.userservice.user;

import com.charlie.hirehub.userservice.user.dto.request.LoginRequest;
import com.charlie.hirehub.userservice.user.dto.request.RegisterUserRequest;
import com.charlie.hirehub.userservice.user.dto.request.UpdateUserRequest;
import com.charlie.hirehub.userservice.user.dto.response.LoginResponse;
import com.charlie.hirehub.userservice.user.dto.response.UserDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    UserDTO registerNewUser(RegisterUserRequest request);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(Long id, @Valid UpdateUserRequest request);

    void deleteUser(Long id);

    LoginResponse login(LoginRequest request);
}
