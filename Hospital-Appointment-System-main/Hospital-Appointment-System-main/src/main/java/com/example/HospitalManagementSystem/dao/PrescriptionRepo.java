package com.example.HospitalManagementSystem.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HospitalManagementSystem.entity.Prescription;

public interface PrescriptionRepo extends JpaRepository<Prescription, Integer> {

    // All prescriptions for a patient (patient dashboard history)
    List<Prescription> findByPatientIdOrderByPrescribedDateDesc(int patientId);

    // All prescriptions written by a doctor for a specific patient (doctor view)
    List<Prescription> findByPatientIdAndDoctorIdOrderByPrescribedDateDesc(int patientId, int doctorId);

    // All prescriptions written by a doctor (for doctor's own records)
    List<Prescription> findByDoctorIdOrderByPrescribedDateDesc(int doctorId);

    // Enforce one-per-appointment: check if a prescription already exists for this appointment
    Optional<Prescription> findByAppointmentId(int appointmentId);
}