package com.example.MovieFlix.service;

import com.example.MovieFlix.model.entities.User;
import com.example.MovieFlix.model.dto.AuthResponse;
import com.example.MovieFlix.model.dto.LoginRequest;
import com.example.MovieFlix.model.dto.RegisterRequest;
import com.example.MovieFlix.repository.UserRepository;
import com.example.MovieFlix.common.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Service handling authentication operations
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Register a new user
     *
     * @param registerRequest the registration request
     * @return AuthResponse with JWT token and user info
     */
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        logger.info("Attempting to register user: {}", registerRequest.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Set default role
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        user.setRoles(roles);
        user.setEnabled(true);

        // Save user
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getUsername());

        // Generate token
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUser.getUsername())
                .password(savedUser.getPassword())
                .roles("USER")
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, savedUser.getUsername());
    }

    /**
     * Login user
     *
     * @param loginRequest the login request
     * @return AuthResponse with JWT token and user info
     */
    public AuthResponse login(LoginRequest loginRequest) {
        logger.info("Attempting to login user: {}", loginRequest.getUsername());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        // Get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate token
        String token = jwtUtil.generateToken(userDetails);

        logger.info("User logged in successfully: {}", loginRequest.getUsername());

        return new AuthResponse(token, loginRequest.getUsername());
    }
}
