package org.example.rideshare.service;

import org.example.rideshare.dto.AuthResponse;
import org.example.rideshare.dto.CreateUserRequest;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.util.JwtUtil;
import org.example.rideshare.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BadRequestException("username_taken");
        }
        if (!req.getRole().equals("ROLE_USER") && !req.getRole().equals("ROLE_DRIVER")) {
            throw new BadRequestException("invalid_role");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(req.getRole());
        userRepository.save(u);
    }

    public AuthResponse login(String username, String password) {
        var opt = userRepository.findByUsername(username);
        if (opt.isEmpty() || !passwordEncoder.matches(password, opt.get().getPassword())) {
            throw new BadRequestException("invalid_credentials");
        }
        User u = opt.get();
        String token = jwtUtil.generateToken(u.getUsername(), u.getRole());
        return new AuthResponse(token, u.getUsername(), u.getRole());
    }
}
