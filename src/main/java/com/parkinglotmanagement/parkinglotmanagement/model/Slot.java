package com.parkinglotmanagement.parkinglotmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Data
@Schema(description = "Represents a parking slot in a parking floor")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the slot", example = "1")
    private Long id;

    @Schema(description = "Number of the slot within the floor", example = "101")
    private int slotNumber;

    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    @Schema(description = "The parking floor this slot belongs to", required = true)
    private ParkingFloor parkingFloor;

    @Version
    @Schema(description = "Version field for optimistic locking", example = "0")
    private Long version;
}
