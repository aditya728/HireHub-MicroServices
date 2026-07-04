package com.charlie.hirehub.userservice.user;

import com.charlie.hirehub.userservice.user.dto.request.RegisterUserRequest;
import com.charlie.hirehub.userservice.user.dto.response.UserDTO;
import jakarta.validation.Valid;

public interface UserService {
    UserDTO registerNewUser(RegisterUserRequest request);
}
