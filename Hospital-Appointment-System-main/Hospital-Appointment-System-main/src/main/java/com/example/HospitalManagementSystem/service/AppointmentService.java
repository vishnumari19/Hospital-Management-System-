package com.example.HospitalManagementSystem.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.example.HospitalManagementSystem.dao.AppointmentRepo;
import com.example.HospitalManagementSystem.dao.DoctorTimeSlotRepo;
import com.example.HospitalManagementSystem.entity.Appointment;
import com.example.HospitalManagementSystem.entity.AppointmentStatus;
import com.example.HospitalManagementSystem.entity.DoctorTimeSlot;

@Service
public class AppointmentService {

    @Autowired AppointmentRepo repo;
    @Autowired DoctorTimeSlotRepo slotRepo;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void bookAppointment(Appointment a) {
        LocalDateTime requested = a.getAppointmentDate();
        int doctorId  = a.getDoctor().getId();
        int patientId = a.getPatient().getId();

        LocalDateTime from = requested.minusMinutes(4);
        LocalDateTime to   = requested.plusMinutes(4);
        LocalDate     day  = requested.toLocalDate();


        // Check 1: Same patient already booked THIS doctor near this time
        List<Appointment> sameDocConflict =
            repo.findPatientConflictWithDoctor(patientId, doctorId, from, to);
        if (!sameDocConflict.isEmpty()) {
            throw new RuntimeException(
                "You already have an appointment with this doctor at or near the selected time.");
        }

        // Check 2: Same patient already has ANY appointment at the same time
        List<Appointment> anyDocConflict =
            repo.findPatientTimeConflictAnyDoctor(patientId, from, to);
        if (!anyDocConflict.isEmpty()) {
            Appointment existing = anyDocConflict.get(0);
            throw new RuntimeException(
                "You already have an appointment with Dr. "
                + existing.getDoctor().getName()
                + " at " + existing.getAppointmentDate().toLocalTime()
                + ". You cannot book two appointments at the same time.");
        }

        // Check 3: Same patient already booked this doctor on the same day
        List<Appointment> sameDayConflict =
            repo.findPatientAppointmentWithDoctorOnDay(patientId, doctorId, day);
        if (!sameDayConflict.isEmpty()) {
            throw new RuntimeException(
                "You already have an appointment with this doctor on "
                + day + ". Only one appointment per doctor per day is allowed.");
        }

        // Check 4: Requested time must be within the doctor's defined working hours
        String    dayName       = requested.getDayOfWeek().name();
        LocalTime requestedTime = requested.toLocalTime();
        List<DoctorTimeSlot> slots = slotRepo.findByDoctorId(doctorId);
        boolean withinSlot = slots.stream().anyMatch(s ->
            s.getDayOfWeek().equalsIgnoreCase(dayName) &&
            !requestedTime.isBefore(s.getStartTime()) &&
            !requestedTime.isAfter(s.getEndTime())
        );
        if (!withinSlot) {
            throw new RuntimeException(
                "Selected time is outside the doctor's available hours.");
        }

        // Check 5: Pessimistic-lock check — blocks two patients booking same slot simultaneously
        List<Appointment> doctorConflict =
            repo.findConflictingAppointmentsWithLock(doctorId, from, to);
        if (!doctorConflict.isEmpty()) {
            throw new RuntimeException(
                "This slot was just taken by another patient. Please choose a different time.");
        }

        // ── ALL CHECKS PASSED — SAVE ─────────────────────────────────────────
        a.setStatus(AppointmentStatus.PENDING);
        repo.save(a);
    }

    public List<Appointment> getAllAppointments()            { return repo.findAll(); }
    public List<Appointment> getDoctorAppointments(int id)  { return repo.findByDoctorId(id); }
    public List<Appointment> getPatientAppointments(int id) { return repo.findByPatientId(id); }

    @Transactional
    public void updateStatus(int appointmentId, AppointmentStatus status) {
        Appointment a = repo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));
        a.setStatus(status);
        repo.save(a);
    }

    public void deleteAppointment(int id) { repo.deleteById(id); }
}