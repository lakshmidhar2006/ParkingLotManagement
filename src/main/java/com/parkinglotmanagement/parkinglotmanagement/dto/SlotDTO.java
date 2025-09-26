package com.parkinglotmanagement.parkinglotmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SlotDTO(

    @Schema(description = "Unique identifier of the slot.", 
            example = "101", 
            accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "The slot number.", 
            example = "101", 
            required = true)
    int slotNumber,

    @Schema(description = "ID of the floor this slot belongs to.", 
            example = "1", 
            accessMode = Schema.AccessMode.READ_ONLY)
    Long floorId
) {}