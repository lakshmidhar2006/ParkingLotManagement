package com.parkinglotmanagement.parkinglotmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Types of vehicles and their hourly rates")
public enum VehicleType {

    @Schema(description = "Four-wheeler vehicle", example = "FOUR_WHEELER")
    FOUR_WHEELER(30.0),

    @Schema(description = "Two-wheeler vehicle", example = "TWO_WHEELER")
    TWO_WHEELER(20.0);

    private final double hourlyRate;

    VehicleType(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }
}
