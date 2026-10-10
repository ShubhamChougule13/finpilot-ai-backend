package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.LoginRequest;
import com.finpilot.finpilotai.dto.RegisterRequest;
import com.finpilot.finpilotai.entity.User;
import com.finpilot.finpilotai.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        userRepository.save(user);

        return "User Registered Successfully";
    }

    public String loginUser(LoginRequest request) {

        Optional<User> user =
                userRepository.findByEmail(
                        request.getEmail()
                );

        if (!user.isPresent()) {
            return "Invalid Email or Password";
        }

        User existingUser = user.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                existingUser.getPassword()
        )) {

            return "Invalid Email or Password";
        }

        return jwtService.generateToken(
                existingUser.getEmail()
        );
    }
}