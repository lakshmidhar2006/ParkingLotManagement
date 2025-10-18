package com.parkinglotmanagement.parkinglotmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkinglotmanagement.parkinglotmanagement.dto.LoginDto;
import com.parkinglotmanagement.parkinglotmanagement.dto.RegisterDto;
import com.parkinglotmanagement.parkinglotmanagement.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles user login requests.
     * @param loginDto DTO containing the username and password.
     * @return A success message upon successful authentication.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles new user registration requests.
     * @param registerDto DTO containing the username and password for the new user.
     * @return A success message upon successful registration.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}