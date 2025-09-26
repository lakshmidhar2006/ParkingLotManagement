package com.parkinglotmanagement.parkinglotmanagement.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkinglotmanagement.parkinglotmanagement.dto.FloorDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationRequestDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationResponseDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.SlotDTO;
import com.parkinglotmanagement.parkinglotmanagement.exception.ResourceNotFoundException;
import com.parkinglotmanagement.parkinglotmanagement.model.ParkingFloor;
import com.parkinglotmanagement.parkinglotmanagement.model.Reservation;
import com.parkinglotmanagement.parkinglotmanagement.model.Slot;
import com.parkinglotmanagement.parkinglotmanagement.repository.FloorRepository;
import com.parkinglotmanagement.parkinglotmanagement.repository.ReservationRepository;
import com.parkinglotmanagement.parkinglotmanagement.repository.SlotRepository;

@Service
public class ParkingService {

    private final FloorRepository floorRepository;
    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    public ParkingService(FloorRepository floorRepository, SlotRepository slotRepository, ReservationRepository reservationRepository) {
        this.floorRepository = floorRepository;
        this.slotRepository = slotRepository;
        this.reservationRepository = reservationRepository;
    }

    // Accepts and returns DTOs
    public FloorDTO createFloor(FloorDTO floorDTO) {
        ParkingFloor floor = new ParkingFloor();
        floor.setFloorNumber(floorDTO.floorNumber());
        ParkingFloor savedFloor = floorRepository.save(floor);
        return convertToFloorDTO(savedFloor);
    }

    // Accepts and returns DTOs
    public SlotDTO createSlot(Long floorId, SlotDTO slotDTO) {
        ParkingFloor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + floorId));
        
        Slot slot = new Slot();
        slot.setSlotNumber(slotDTO.slotNumber());
        slot.setParkingFloor(floor);
        Slot savedSlot = slotRepository.save(slot);
        return convertToSlotDTO(savedSlot);
    }

    @Transactional // Ensures the entire method is one safe database transaction
    public ReservationResponseDTO reserveSlot(ReservationRequestDTO requestDTO) {
        // Logic is moved from the controller to the service
        Slot slot = slotRepository.findById(requestDTO.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + requestDTO.getSlotId()));

        Reservation reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setStartTime(requestDTO.getStartTime());
        reservation.setEndTime(requestDTO.getEndTime());
        reservation.setVehicleNumber(requestDTO.getVehicleNumber());
        reservation.setVehicleType(requestDTO.getVehicleType());

        // Business rule validation
        if (!reservation.getStartTime().isBefore(reservation.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        if (Duration.between(reservation.getStartTime(), reservation.getEndTime()).toHours() >= 24) {
            throw new IllegalArgumentException("Reservation duration cannot exceed 24 hours.");
        }
        
        List<Reservation> overlaps = reservationRepository.findOverlappingReservations(
                reservation.getSlot().getId(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );
        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("Slot is already booked for the selected time range.");
        }
        
        // Cost calculation
        double durationInMinutes = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes();
        long roundedHours = (long) Math.ceil(durationInMinutes / 60.0);
        double cost = roundedHours * reservation.getVehicleType().getHourlyRate();
        reservation.setCost(cost);
        
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // This save call is necessary to trigger the @Version increment for optimistic locking
        slotRepository.save(slot);
        
        return convertToReservationResponseDTO(savedReservation);
    }

    // Returns a DTO
    public ReservationResponseDTO getReservationDetails(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));
        return convertToReservationResponseDTO(reservation);
    }

    public void cancelReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + reservationId);
        }
        reservationRepository.deleteById(reservationId);
    }

    // Returns a Page of DTOs
    public Page<SlotDTO> getAvailableSlots(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        List<Long> occupiedSlotIds = reservationRepository.findOccupiedSlotIds(startTime, endTime);
        
        Page<Slot> slotPage;
        if (occupiedSlotIds.isEmpty()) {
            slotPage = slotRepository.findAll(pageable);
        } else {
            slotPage = slotRepository.findByIdNotIn(occupiedSlotIds, pageable);
        }
        // Map the Page<Slot> to a Page<SlotDTO>
        return slotPage.map(this::convertToSlotDTO);
    }

    // --- Helper methods for converting Entities to DTOs ---

    private FloorDTO convertToFloorDTO(ParkingFloor floor) {
        return new FloorDTO(floor.getId(), floor.getFloorNumber());
    }

    private SlotDTO convertToSlotDTO(Slot slot) {
        return new SlotDTO(slot.getId(), slot.getSlotNumber(), slot.getParkingFloor().getId());
    }

    private ReservationResponseDTO convertToReservationResponseDTO(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getSlot().getId(),
                reservation.getSlot().getSlotNumber(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getVehicleNumber(),
                reservation.getVehicleType(),
                reservation.getCost()
        );
    }
}