package com.parkinglotmanagement.parkinglotmanagement.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@Schema(description = "Represents a reservation of a parking slot for a vehicle")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the reservation", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    @Schema(description = "The slot reserved", required = true)
    private Slot slot;

    @Column(nullable = false)
    @Schema(description = "Reservation start time", example = "2025-09-26T09:00:00")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @Schema(description = "Reservation end time", example = "2025-09-26T12:00:00")
    private LocalDateTime endTime;

    @Column(nullable = false)
    @Schema(description = "Vehicle number for the reservation", example = "KA01AB1234")
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Type of vehicle", example = "CAR")
    private VehicleType vehicleType;

    @Schema(description = "Calculated cost of the reservation", example = "150.0")
    private double cost;
}
