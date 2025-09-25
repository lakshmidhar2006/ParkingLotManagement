package com.parkinglotmanagement.parkinglotmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.parkinglotmanagement.parkinglotmanagement.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId AND r.startTime < :endTime AND r.endTime > :startTime")
    List<Reservation> findOverlappingReservations(
        @Param("slotId") Long slotId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}