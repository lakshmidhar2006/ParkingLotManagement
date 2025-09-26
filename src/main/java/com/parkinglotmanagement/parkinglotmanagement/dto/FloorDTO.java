package com.parkinglotmanagement.parkinglotmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FloorDTO(
    
    @Schema(description = "Unique identifier of the floor.", 
            example = "1", 
            accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    
    @Schema(description = "The floor number.", 
            example = "1", 
            required = true)
    int floorNumber
) {}