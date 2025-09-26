package com.parkinglotmanagement.parkinglotmanagement.controller;

import com.parkinglotmanagement.parkinglotmanagement.dto.FloorDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationRequestDTO;
import com.parkinglotmanagement.parkinglotmanagement.dto.ReservationResponseDTO;
import com.parkinglotmanagement.parkinglotmanagement.model.VehicleType;
import com.parkinglotmanagement.parkinglotmanagement.service.ParkingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ParkingController.class, WelcomeController.class}) // Test both controllers
class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingService parkingService; // Controller only depends on the service

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void whenCreateFloor_thenReturns201() throws Exception {
        FloorDTO floorRequest = new FloorDTO(null, 1);
        FloorDTO floorResponse = new FloorDTO(1L, 1);

        when(parkingService.createFloor(any(FloorDTO.class))).thenReturn(floorResponse);

        mockMvc.perform(post("/api/floors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(floorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.floorNumber").value(1));
    }

    @Test
    void whenGetReservationDetails_withValidId_thenReturns200() throws Exception {
        ReservationResponseDTO responseDTO = new ReservationResponseDTO(
            1L, 1L, 101, LocalDateTime.now(), LocalDateTime.now().plusHours(2),
            "TS09AB1234", VehicleType.TWO_WHEELER, 60.0
        );

        when(parkingService.getReservationDetails(1L)).thenReturn(responseDTO);
        
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vehicleNumber").value("TS09AB1234"));
    }

    @Test
    void whenReserveSlot_withInvalidVehicleNumber_thenReturns400() throws Exception {
        ReservationRequestDTO invalidRequest = new ReservationRequestDTO(
            1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2),
            "INVALID-123", VehicleType.FOUR_WHEELER
        );

        mockMvc.perform(post("/api/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCancelReservation_thenReturns204() throws Exception {
        doNothing().when(parkingService).cancelReservation(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());
    }
}