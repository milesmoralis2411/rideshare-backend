package org.example.rideshare.controller;

import org.example.rideshare.dto.AuthResponse;
import org.example.rideshare.dto.CreateUserRequest;
import org.example.rideshare.dto.LoginRequest;
import org.example.rideshare.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest req) {
        authService.register(req);
        return ResponseEntity.ok().body(java.util.Map.of("message","registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse resp = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(resp);
    }
}
