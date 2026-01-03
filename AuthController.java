package com.smartsla.smart_sla_tracker.controller;

import com.smartsla.smart_sla_tracker.dto.LoginRequest;
import com.smartsla.smart_sla_tracker.entity.AppUser;
import com.smartsla.smart_sla_tracker.entity.Role;
import com.smartsla.smart_sla_tracker.repository.UserRepository;
import com.smartsla.smart_sla_tracker.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AppUser user) {

        // ✅ Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(token);
    }
}


