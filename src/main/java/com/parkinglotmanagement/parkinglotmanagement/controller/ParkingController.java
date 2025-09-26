package com.parkinglotmanagement.parkinglotmanagement.controller;

import com.parkinglotmanagement.parkinglotmanagement.dto.FloorDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationRequestDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationResponseDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.SlotDTO;
import com.parkinglotmanagement.parkinglotmanagement.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@Tag(name = "Parking API", description = "Endpoints for Parking Lot Management")
public class ParkingController {

    //  The controller only depends on the service layer.
    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }
    
    @PostMapping("/floors")
    @Operation(summary = "Create a parking floor", description = "Creates a new parking floor")
    //  Using DTOs for request and response to decouple the API from the database schema.
    public ResponseEntity<FloorDTO> createFloor(@RequestBody FloorDTO floorDTO) {
        FloorDTO createdFloor = parkingService.createFloor(floorDTO);
        return new ResponseEntity<>(createdFloor, HttpStatus.CREATED);
    }

    //  Using more RESTful URL for nested resources.
    @PostMapping("/floors/{floorId}/slots")
    @Operation(summary = "Create a parking slot", description = "Creates a new slot for a specific floor")
    public ResponseEntity<SlotDTO> createSlot(@PathVariable Long floorId, @RequestBody SlotDTO slotDTO) {
        SlotDTO createdSlot = parkingService.createSlot(floorId, slotDTO);
        return new ResponseEntity<>(createdSlot, HttpStatus.CREATED);
    }

    @GetMapping("/availability")
    @Operation(summary = "Get available slots", description = "Lists available slots for a given time range with pagination")
    public ResponseEntity<Page<SlotDTO>> getAvailableSlots(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            Pageable pageable) {
        Page<SlotDTO> availableSlots = parkingService.getAvailableSlots(startTime, endTime, pageable);
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve a slot", description = "Reserves a parking slot for a given time range")
    public ResponseEntity<ReservationResponseDTO> reserveSlot(@Valid @RequestBody ReservationRequestDTO requestDTO) {
        // All logic is now handled by the service layer.
        ReservationResponseDTO createdReservation = parkingService.reserveSlot(requestDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @GetMapping("/reservations/{id}")
    @Operation(summary = "Get reservation details", description = "Fetches reservation details by ID")
    public ResponseEntity<ReservationResponseDTO> getReservationDetails(@PathVariable Long id) {
        ReservationResponseDTO reservationDetails = parkingService.getReservationDetails(id);
        return ResponseEntity.ok(reservationDetails);
    }

    @DeleteMapping("/reservations/{id}")
    @Operation(summary = "Cancel a reservation", description = "Cancels a reservation by ID")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        parkingService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}