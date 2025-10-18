package com.parkinglotmanagement.parkinglotmanagement.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ParkingAPIException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public ParkingAPIException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}