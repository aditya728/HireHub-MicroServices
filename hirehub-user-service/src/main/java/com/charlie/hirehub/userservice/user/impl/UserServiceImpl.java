package com.charlie.hirehub.userservice.user.impl;

import com.charlie.hirehub.userservice.user.User;
import com.charlie.hirehub.userservice.user.UserRepository;
import com.charlie.hirehub.userservice.user.UserService;
import com.charlie.hirehub.userservice.user.dto.request.LoginRequest;
import com.charlie.hirehub.userservice.user.dto.request.RegisterUserRequest;
import com.charlie.hirehub.userservice.user.dto.request.UpdateUserRequest;
import com.charlie.hirehub.userservice.user.dto.response.LoginResponse;
import com.charlie.hirehub.userservice.user.dto.response.UserDTO;
import com.charlie.hirehub.userservice.user.enums.Role;
import com.charlie.hirehub.userservice.user.exception.UserAlreadyExistsException;
import com.charlie.hirehub.userservice.user.exception.UserNotFoundException;
import com.charlie.hirehub.userservice.user.mapper.UserMapper;
import com.charlie.hirehub.userservice.user.security.AuthenticatedUser;
import com.charlie.hirehub.userservice.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

        //Hashing the password before saving it in the DB
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userRepo.save(user);

        logger.info("Successfully registered user with id {} and email {}",
                savedUser.getId(), userEmail);

        return UserMapper.toUserDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {

        logger.info("Fetching user with id {}", id);

        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        logger.info("Successfully fetched user with id {} ", id);

        return UserMapper.toUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {

        logger.info("Fetching all users");

        List<UserDTO> users = userRepo.findAll()
                .stream()
                .map(UserMapper::toUserDTO)
                .toList();

        logger.info("Successfully fetched {} users", users.size());

        return users;
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest request) {

        logger.info("Updating user with id {}", id);

        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id " + id + " not found."));

        if (!user.getEmail().equals(request.getEmail())
                && userRepo.existsByEmail(request.getEmail())) {

            logger.warn("User with email {} already exists", request.getEmail());

            throw new UserAlreadyExistsException(
                    "User with email " + request.getEmail() + " already exists.");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        User updatedUser = userRepo.save(user);

        logger.info("Successfully updated user with id {}", id);

        return UserMapper.toUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        logger.info("Deleting user with id {}", id);

        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id " + id + " not found."));

        userRepo.delete(user);

        logger.info("Successfully deleted user with id {}", id);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        logger.info("Performing login with email {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(authenticatedUser);

        logger.info("Login successful for email {}", request.getEmail());

        return generateLoginResponseWithUser(authenticatedUser, jwtToken);
    }

    private LoginResponse generateLoginResponseWithUser(AuthenticatedUser authenticatedUser, String jwtToken){

        logger.info("Login successful with email {}", authenticatedUser.getUser().getEmail());
        return new LoginResponse(
                jwtToken,
                authenticatedUser.getUser().getEmail(),
                authenticatedUser.getUser().getRole(),
                "Login successful."
        );
    }
}
