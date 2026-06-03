package com.example.HospitalManagementSystem.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HospitalManagementSystem.entity.Patient;

public interface PatientRepo extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByUserEmail(String email);
}