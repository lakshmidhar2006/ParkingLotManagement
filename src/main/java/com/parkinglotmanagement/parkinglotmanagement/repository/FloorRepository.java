package com.parkinglotmanagement.parkinglotmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkinglotmanagement.parkinglotmanagement.model.ParkingFloor;

public interface FloorRepository extends JpaRepository<ParkingFloor, Long> {
}