package com.parkinglotmanagement.parkinglotmanagement.dto;

import java.time.LocalDateTime;

import com.parkinglotmanagement.parkinglotmanagement.model.VehicleType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Details of a created reservation.")
public record ReservationResponseDTO(

    @Schema(description = "Unique identifier of the reservation.", example = "1")
    Long id,

    @Schema(description = "ID of the reserved slot.", example = "101")
    Long slotId,

    @Schema(description = "Number of the reserved slot.", example = "101")
    int slotNumber,

    @Schema(description = "Reservation start time.", example = "2025-09-26T15:00:00")
    LocalDateTime startTime,

    @Schema(description = "Reservation end time.", example = "2025-09-26T18:00:00")
    LocalDateTime endTime,

    @Schema(description = "Vehicle registration number.", example = "KA01AB1234")
    String vehicleNumber,

    @Schema(description = "Type of the vehicle.", example = "FOUR_WHEELER")
    VehicleType vehicleType,

    @Schema(description = "Total calculated cost for the reservation.", example = "90.0")
    double cost
) {}