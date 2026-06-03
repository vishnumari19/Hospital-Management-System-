package com.example.HospitalManagementSystem.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HospitalManagementSystem.entity.DoctorTimeSlot;

public interface DoctorTimeSlotRepo extends JpaRepository<DoctorTimeSlot, Integer> {
    List<DoctorTimeSlot> findByDoctorId(int doctorId);
    void deleteByDoctorId(int doctorId);
}