package com.charlie.hirehub.userservice.user.impl;

import com.charlie.hirehub.userservice.user.User;
import com.charlie.hirehub.userservice.user.UserRepository;
import com.charlie.hirehub.userservice.user.UserService;
import com.charlie.hirehub.userservice.user.dto.request.RegisterUserRequest;
import com.charlie.hirehub.userservice.user.dto.response.UserDTO;
import com.charlie.hirehub.userservice.user.exception.UserAlreadyExistsException;
import com.charlie.hirehub.userservice.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDTO registerNewUser(RegisterUserRequest request) {

        String userEmail = request.getEmail();
        logger.info("Registering new user with email {}", userEmail);

        boolean userExists = userRepo.existsByEmail(userEmail);

        if(userExists){
            logger.warn("User with email {} already exists", userEmail);
            throw new UserAlreadyExistsException("User with email " + userEmail + " already exists.");
        }

        User user = UserMapper.toEntity(request);
        User savedUser = userRepo.save(user);

        logger.info("Successfully registered user with id {} and email {}",
                savedUser.getId(), userEmail);

        return UserMapper.toUserDTO(savedUser);
    }
}
