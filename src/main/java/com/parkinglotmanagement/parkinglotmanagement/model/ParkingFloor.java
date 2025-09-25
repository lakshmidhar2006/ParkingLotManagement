package com.parkinglotmanagement.parkinglotmanagement.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
@Schema(description = "Represents a parking floor containing multiple parking slots")
public class ParkingFloor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the parking floor", example = "1")
    private Long id;

    @Schema(description = "Floor number of the parking floor", example = "2")
    private int floorNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "parkingFloor", cascade = CascadeType.ALL)
    @Schema(description = "List of slots in the floor (read-only)", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Slot> slots;
}
