package com.charlie.hirehub.userservice.user;

import com.charlie.hirehub.userservice.user.dto.request.RegisterUserRequest;
import com.charlie.hirehub.userservice.user.dto.response.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
POST   /users/register
GET    /users/{id}
GET    /users
PUT    /users/{id}
DELETE /users/{id}
 */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerNewUser(
                @Valid @RequestBody RegisterUserRequest request){

        UserDTO user = userService.registerNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
