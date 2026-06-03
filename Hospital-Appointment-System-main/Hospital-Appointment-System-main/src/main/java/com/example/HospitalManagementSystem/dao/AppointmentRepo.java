package com.example.HospitalManagementSystem.dao;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.HospitalManagementSystem.entity.Appointment;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByDoctorId(int id);
    List<Appointment> findByPatientId(int id);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a FROM Appointment a
        WHERE a.doctor.id = :doctorId
          AND a.appointmentDate BETWEEN :from AND :to
        """)
    List<Appointment> findConflictingAppointmentsWithLock(
        @Param("doctorId") int doctorId,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );


    @Query("""
        SELECT a FROM Appointment a
        WHERE a.patient.id = :patientId
          AND a.doctor.id = :doctorId
          AND a.appointmentDate BETWEEN :from AND :to
          AND a.status <> com.example.HospitalManagementSystem.entity.AppointmentStatus.REJECTED
        """)
    List<Appointment> findPatientConflictWithDoctor(
        @Param("patientId") int patientId,
        @Param("doctorId") int doctorId,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.patient.id = :patientId
          AND a.appointmentDate BETWEEN :from AND :to
          AND a.status <> com.example.HospitalManagementSystem.entity.AppointmentStatus.REJECTED
        """)
    List<Appointment> findPatientTimeConflictAnyDoctor(
        @Param("patientId") int patientId,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );

 
    @Query("""
        SELECT a FROM Appointment a
        WHERE a.patient.id = :patientId
          AND a.doctor.id = :doctorId
          AND CAST(a.appointmentDate AS date) = :day
          AND a.status <> com.example.HospitalManagementSystem.entity.AppointmentStatus.REJECTED
        """)
    List<Appointment> findPatientAppointmentWithDoctorOnDay(
        @Param("patientId") int patientId,
        @Param("doctorId") int doctorId,
        @Param("day") LocalDate day
    );
}