package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.VehicleType;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "Slot ID cannot be null")
    private Long slotId;

    @NotNull(message = "Start time cannot be null")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;
    
    @NotNull(message = "End time cannot be null")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotBlank(message = "Vehicle number cannot be blank")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$", message = "Vehicle number must match the format XX00XX0000")
    private String vehicleNumber;
    
    @NotNull(message = "Vehicle type cannot be null")
    private VehicleType vehicleType;
}