package com.example.hireme.controller;

import com.example.hireme.model.User;
import com.example.hireme.repository.UserRepository;
import com.example.hireme.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.hireme.service.UserService;

import java.util.HashMap;   
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService UserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // -------- SIGNUP ----------
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        // check if email already exists
        //System.out.println("Signup endpoint hit!"); // debug log

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        User savedUser = UserService.register(user);
        return ResponseEntity.ok(savedUser);
    }

    // -------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginRequest) {
        // Step 1: Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );

        // Step 2: Retrieve full user object from DB
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 3: Generate token with full user details
        String token = jwtUtil.generateToken(user);

        // Step 4: Build response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }
}

