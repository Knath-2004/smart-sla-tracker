package com.smartsla.smart_sla_tracker.controller;

import com.smartsla.smart_sla_tracker.entity.AppUser;
import com.smartsla.smart_sla_tracker.entity.Role;
import com.smartsla.smart_sla_tracker.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AppUser user) {

        user.setRole(Role.USER);   // ✅ now USER exists
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
