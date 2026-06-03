package com.example.HospitalManagementSystem.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HospitalManagementSystem.entity.Doctor;

public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByUserEmail(String email);
    List<Doctor> findAllByApprovedFalse();
    List<Doctor> findAllByApprovedTrue();
}