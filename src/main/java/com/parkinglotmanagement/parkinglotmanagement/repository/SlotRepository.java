package com.parkinglotmanagement.parkinglotmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkinglotmanagement.parkinglotmanagement.model.Slot;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByParkingFloorId(Long floorId);
}