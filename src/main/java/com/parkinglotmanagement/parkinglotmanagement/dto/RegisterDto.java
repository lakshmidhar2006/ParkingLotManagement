package com.parkinglotmanagement.parkinglotmanagement.dto;

import java.util.Set;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String password;
    private Set<String> roles;
}