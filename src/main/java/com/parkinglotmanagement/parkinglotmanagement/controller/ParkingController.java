package com.parkinglotmanagement.parkinglotmanagement.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // ADD THIS IMPORT
import org.springframework.web.bind.annotation.RestController;

import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationRequestDTO;
import com.parkinglotmanagement.parkinglotmanagement.model.ParkingFloor;
import com.parkinglotmanagement.parkinglotmanagement.model.Reservation;
import com.parkinglotmanagement.parkinglotmanagement.model.Slot;
import com.parkinglotmanagement.parkinglotmanagement.repository.SlotRepository;
import com.parkinglotmanagement.parkinglotmanagement.service.ParkingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Parking API", description = "Endpoints for Parking Lot Management")
public class ParkingController {

    private final ParkingService parkingService;
    private final SlotRepository slotRepository;

    public ParkingController(ParkingService parkingService, SlotRepository slotRepository) {
        this.parkingService = parkingService;
        this.slotRepository = slotRepository;
    }

    @PostMapping("/floors")
    @Operation(summary = "Create a parking floor", description = "Creates a new parking floor")
    public ResponseEntity<ParkingFloor> createFloor(@RequestBody ParkingFloor floor) {
        return ResponseEntity.ok(parkingService.createFloor(floor));
    }

    @PostMapping("/slots")
    @Operation(summary = "Create a parking slot", description = "Creates a new slot for a specific floor")
    public ResponseEntity<Slot> createSlot(@RequestParam Long floorId, @RequestBody Slot slot) {
        return ResponseEntity.ok(parkingService.createSlot(floorId, slot));
    }

    @GetMapping("/availability")
    @Operation(summary = "Get available slots", description = "Lists available slots for a given time range with pagination")
    public ResponseEntity<Page<Slot>> getAvailableSlots(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            Pageable pageable) {
        Page<Slot> availableSlots = parkingService.getAvailableSlots(startTime, endTime, pageable);
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve a slot", description = "Reserves a parking slot for a given time range")
    public ResponseEntity<Reservation> reserveSlot(@Valid @RequestBody ReservationRequestDTO requestDTO) {
        Slot slot = slotRepository.findById(requestDTO.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + requestDTO.getSlotId()));

        Reservation reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setStartTime(requestDTO.getStartTime());
        reservation.setEndTime(requestDTO.getEndTime());
        reservation.setVehicleNumber(requestDTO.getVehicleNumber());
        reservation.setVehicleType(requestDTO.getVehicleType());

        Reservation createdReservation = parkingService.reserveSlot(reservation);
        return ResponseEntity.ok(createdReservation);
    }

    @GetMapping("/reservations/{id}")
    @Operation(summary = "Get reservation details", description = "Fetches reservation details by ID")
    public ResponseEntity<Reservation> getReservationDetails(@PathVariable Long id) {
        return ResponseEntity.ok(parkingService.getReservationDetails(id));
    }

    @DeleteMapping("/reservations/{id}")
    @Operation(summary = "Cancel a reservation", description = "Cancels a reservation by ID")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        parkingService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}