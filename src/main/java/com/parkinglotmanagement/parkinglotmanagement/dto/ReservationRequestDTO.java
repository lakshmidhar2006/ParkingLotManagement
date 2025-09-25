package com.parkinglotmanagement.parkinglotmanagement.dto;

import java.time.LocalDateTime;

import com.parkinglotmanagement.parkinglotmanagement.model.VehicleType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "Slot ID cannot be null")
    @Schema(description = "ID of the parking slot to reserve", example = "1", required = true)
    private Long slotId;

    @NotNull(message = "Start time cannot be null")
    @Future(message = "Start time must be in the future")
    @Schema(description = "Reservation start time (must be in the future)", 
            example = "2025-09-25T15:00:00", required = true)
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @Future(message = "End time must be in the future")
    @Schema(description = "Reservation end time (must be in the future)", 
            example = "2025-09-25T18:00:00", required = true)
    private LocalDateTime endTime;

    @NotBlank(message = "Vehicle number cannot be blank")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$", message = "Vehicle number must match the format XX00XX0000")
    @Schema(description = "Vehicle registration number in format XX00XX0000", 
            example = "KA01AB1234", required = true)
    private String vehicleNumber;

    @NotNull(message = "Vehicle type cannot be null")
    @Schema(description = "Type of the vehicle (CAR, BIKE, etc.)", example = "CAR", required = true)
    private VehicleType vehicleType;
}
