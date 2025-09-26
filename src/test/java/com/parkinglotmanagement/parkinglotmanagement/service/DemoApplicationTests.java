package com.parkinglotmanagement.parkinglotmanagement.service;

import com.parkinglotmanagement.parkinglotmanagement.dto.FloorDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationRequestDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationResponseDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.SlotDTO;
import com.parkinglotmanagement.parkinglotmanagement.exception.ResourceNotFoundException;
import com.parkinglotmanagement.parkinglotmanagement.model.ParkingFloor;
import com.parkinglotmanagement.parkinglotmanagement.model.Reservation;
import com.parkinglotmanagement.parkinglotmanagement.model.Slot;
import com.parkinglotmanagement.parkinglotmanagement.model.VehicleType;
import com.parkinglotmanagement.parkinglotmanagement.repository.FloorRepository;
import com.parkinglotmanagement.parkinglotmanagement.repository.ReservationRepository;
import com.parkinglotmanagement.parkinglotmanagement.repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private SlotRepository slotRepository;
    @Mock
    private FloorRepository floorRepository;

    @InjectMocks
    private ParkingService parkingService;

    private Slot availableSlot;
    private ReservationRequestDTO reservationRequestDTO;
    private ParkingFloor floor;

    @BeforeEach
    void setUp() {
        floor = new ParkingFloor();
        floor.setId(1L);
        floor.setFloorNumber(1);

        availableSlot = new Slot();
        availableSlot.setId(1L);
        availableSlot.setSlotNumber(101);
        availableSlot.setParkingFloor(floor);
        
        reservationRequestDTO = new ReservationRequestDTO(
            1L,
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(3),
            "KA01AB1234",
            VehicleType.FOUR_WHEELER
        );
    }

    @Test
    void whenCreateFloor_thenSucceeds() {
        when(floorRepository.save(any(ParkingFloor.class))).thenReturn(floor);
        FloorDTO result = parkingService.createFloor(new FloorDTO(null, 1));
        assertNotNull(result);
        assertEquals(1, result.floorNumber());
    }

    @Test
    void whenCreateSlot_thenSucceeds() {
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        when(slotRepository.save(any(Slot.class))).thenReturn(availableSlot);
        SlotDTO result = parkingService.createSlot(1L, new SlotDTO(null, 101, null));
        assertNotNull(result);
        assertEquals(101, result.slotNumber());
    }
    
    @Test
    void whenSlotIsAvailable_thenReservationSucceeds() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(availableSlot));
        when(reservationRepository.findOverlappingReservations(any(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationResponseDTO result = parkingService.reserveSlot(reservationRequestDTO);

        assertNotNull(result);
        assertEquals(60.0, result.cost());
        verify(slotRepository, times(1)).save(availableSlot); // Verify optimistic lock save is called
    }

    @Test
    void whenSlotIsAlreadyBooked_thenReservationFails() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(availableSlot));
        when(reservationRepository.findOverlappingReservations(any(), any(), any())).thenReturn(Collections.singletonList(new Reservation()));

        assertThrows(IllegalStateException.class, () -> parkingService.reserveSlot(reservationRequestDTO));
    }

    @Test
    void whenCancelReservation_thenSucceeds() {
        when(reservationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(1L);
        assertDoesNotThrow(() -> parkingService.cancelReservation(1L));
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenCancelNonExistentReservation_thenFails() {
        when(reservationRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> parkingService.cancelReservation(99L));
    }
}