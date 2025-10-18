package com.parkinglotmanagement.parkinglotmanagement.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parkinglotmanagement.parkinglotmanagement.dto.LoginDto;
import com.parkinglotmanagement.parkinglotmanagement.dto.RegisterDto;
import com.parkinglotmanagement.parkinglotmanagement.exception.ParkingAPIException;
import com.parkinglotmanagement.parkinglotmanagement.model.Role;
import com.parkinglotmanagement.parkinglotmanagement.model.User;
import com.parkinglotmanagement.parkinglotmanagement.repository.RoleRepository;
import com.parkinglotmanagement.parkinglotmanagement.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "User logged-in successfully!";
    }

    // In AuthServiceImpl.java

@Override
public String register(RegisterDto registerDto) {

    // check if username is already in the database
    if(userRepository.existsByUsername(registerDto.getUsername())){
        throw new ParkingAPIException(HttpStatus.BAD_REQUEST, "Username already exists!.");
    }

    User user = new User();
    user.setUsername(registerDto.getUsername());
    // Encrypt the password before saving
    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

    Set<Role> roles = new HashSet<>();
    
    // NEW LOGIC: Check which roles the user wants to be registered with
    if (registerDto.getRoles() != null) {
        registerDto.getRoles().forEach(roleName -> {
            Role userRole = roleRepository.findByName(roleName.toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        });
    } else {
        // Default to USER role if none are provided
        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
    }
    
    user.setRoles(roles);
    userRepository.save(user);

    return "User registered successfully!";
}
}