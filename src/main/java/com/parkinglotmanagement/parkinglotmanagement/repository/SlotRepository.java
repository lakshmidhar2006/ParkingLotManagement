package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Slot;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByParkingFloorId(Long floorId);
}