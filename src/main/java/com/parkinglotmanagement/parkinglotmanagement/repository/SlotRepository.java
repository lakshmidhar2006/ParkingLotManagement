package com.parkinglotmanagement.parkinglotmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parkinglotmanagement.parkinglotmanagement.model.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByParkingFloorId(Long floorId);
    Page<Slot> findByIdNotIn(List<Long> occupiedSlotIds, Pageable pageable);
}