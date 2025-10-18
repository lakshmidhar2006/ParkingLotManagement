package com.parkinglotmanagement.parkinglotmanagement.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parkinglotmanagement.parkinglotmanagement.dto.FloorDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationRequestDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationResponseDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.SlotDTO;
import com.parkinglotmanagement.parkinglotmanagement.service.ParkingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
    @PreAuthorize("hasAuthority('PARKING_LOT_MANAGER')")
    public ResponseEntity<FloorDTO> createFloor(@RequestBody FloorDTO floorDTO) {
        FloorDTO createdFloor = parkingService.createFloor(floorDTO);
        return new ResponseEntity<>(createdFloor, HttpStatus.CREATED);
    }

    //  Using more RESTful URL for nested resources.
    @PostMapping("/floors/{floorId}/slots")
    @Operation(summary = "Create a parking slot", description = "Creates a new slot for a specific floor")
    @PreAuthorize("hasAuthority('PARKING_LOT_MANAGER')")
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
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Reserve a slot", description = "Reserves a parking slot for a given time range")
    public ResponseEntity<ReservationResponseDTO> reserveSlot(@Valid @RequestBody ReservationRequestDTO requestDTO) {
        // All logic is now handled by the service layer.
        ReservationResponseDTO createdReservation = parkingService.reserveSlot(requestDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @GetMapping("/reservations/{id}")
    @Operation(summary = "Get reservation details", description = "Fetches reservation details by ID")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReservationResponseDTO> getReservationDetails(@PathVariable Long id) {
        ReservationResponseDTO reservationDetails = parkingService.getReservationDetails(id);
        return ResponseEntity.ok(reservationDetails);
    }

    @DeleteMapping("/reservations/{id}")
    @Operation(summary = "Cancel a reservation", description = "Cancels a reservation by ID")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        parkingService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}