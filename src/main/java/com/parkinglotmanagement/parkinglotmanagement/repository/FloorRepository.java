package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ParkingFloor;

public interface FloorRepository extends JpaRepository<ParkingFloor, Long> {
}