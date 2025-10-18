package com.parkinglotmanagement.parkinglotmanagement.service;

import com.parkinglotmanagement.parkinglotmanagement.dto.LoginDto;
import com.parkinglotmanagement.parkinglotmanagement.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}