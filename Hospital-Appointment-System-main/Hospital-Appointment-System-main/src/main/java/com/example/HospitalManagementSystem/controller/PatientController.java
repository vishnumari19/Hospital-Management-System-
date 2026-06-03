package com.example.HospitalManagementSystem.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.HospitalManagementSystem.dao.DoctorRepo;
import com.example.HospitalManagementSystem.entity.*;
import com.example.HospitalManagementSystem.service.*;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired DoctorRepo           doctorRepo;
    @Autowired PatientService       patientService;
    @Autowired AppointmentService   appointmentService;
    @Autowired DoctorTimeSlotService slotService;
    @Autowired PrescriptionService  prescriptionService;   
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Patient patient = patientService.getPatientByEmail(principal.getName()).orElse(null);

        if (patient == null) {
            model.addAttribute("error", "Patient profile not found.");
            model.addAttribute("appointments",          Collections.emptyList());
            model.addAttribute("recentPrescriptions",   Collections.emptyList());
            model.addAttribute("totalCount",       0);
            model.addAttribute("pendingCount",     0);
            model.addAttribute("approvedCount",    0);
            model.addAttribute("prescriptionCount", 0);
        } else {
            List<Appointment> appts =
                    appointmentService.getPatientAppointments(patient.getId());

           
            List<com.example.HospitalManagementSystem.entity.Prescription> allRx =
                    prescriptionService.getByPatient(patient.getId());
            List<com.example.HospitalManagementSystem.entity.Prescription> recentRx =
                    allRx.size() > 3 ? allRx.subList(0, 3) : allRx;

            model.addAttribute("patient",      patient);
            model.addAttribute("appointments", appts);
            model.addAttribute("recentPrescriptions", recentRx);
            model.addAttribute("totalCount",        appts.size());
            model.addAttribute("pendingCount",
                    appts.stream().filter(ap -> ap.getStatus() == AppointmentStatus.PENDING).count());
            model.addAttribute("approvedCount",
                    appts.stream().filter(ap -> ap.getStatus() == AppointmentStatus.APPROVED).count());
            model.addAttribute("prescriptionCount", allRx.size());
        }
        return "patient-dashboard";
    }

    @GetMapping("/book")
    public String bookPage(@RequestParam(required = false) Integer doctorId, Model model) {
        List<Doctor> approvedDoctors = doctorRepo.findAll()
                .stream().filter(Doctor::isApproved).toList();
        model.addAttribute("doctors", approvedDoctors);

        if (doctorId != null) {
            model.addAttribute("selectedDoctorId", doctorId);
            model.addAttribute("slots", slotService.getSlotsByDoctor(doctorId));
        }
        return "book-appointment";
    }

    @PostMapping("/book")
    public String book(@RequestParam int doctorId,
                       @RequestParam String appointmentDate,
                       @RequestParam(required = false) String notes,
                       Principal principal,
                       Model model) {
        try {
            Patient patient = patientService.getPatientByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            Appointment a = new Appointment();
            a.setDoctor(doctorRepo.findById(doctorId).orElseThrow());
            a.setPatient(patient);
            a.setAppointmentDate(LocalDateTime.parse(appointmentDate));
            a.setNotes(notes);
            appointmentService.bookAppointment(a);
            return "redirect:/patient/dashboard";

        } catch (RuntimeException e) {
            List<Doctor> approvedDoctors = doctorRepo.findAll()
                    .stream().filter(Doctor::isApproved).toList();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("doctors", approvedDoctors);
            model.addAttribute("selectedDoctorId", doctorId);
            model.addAttribute("slots", slotService.getSlotsByDoctor(doctorId));
            model.addAttribute("selectedDate",  appointmentDate);
            model.addAttribute("selectedNotes", notes);
            return "book-appointment";
        }
    }
}