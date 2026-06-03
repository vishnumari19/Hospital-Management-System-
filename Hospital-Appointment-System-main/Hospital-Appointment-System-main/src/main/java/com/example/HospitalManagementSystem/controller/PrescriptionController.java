package com.example.HospitalManagementSystem.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.HospitalManagementSystem.dao.AppointmentRepo;
import com.example.HospitalManagementSystem.dao.PrescriptionRepo;
import com.example.HospitalManagementSystem.entity.*;
import com.example.HospitalManagementSystem.service.*;

@Controller
public class PrescriptionController {

    @Autowired PrescriptionService prescriptionService;
    @Autowired PatientService      patientService;
    @Autowired DoctorService       doctorService;
    @Autowired AppointmentRepo     appointmentRepo;
    @Autowired PrescriptionRepo    prescriptionRepo;   

    
    @GetMapping("/doctor/prescription/new/{appointmentId}")
    public String prescriptionForm(@PathVariable int appointmentId, Model model, Principal principal) {
        Doctor doctor = doctorService.getDoctorByEmail(principal.getName()).orElseThrow();
        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

     
        if (appt.getDoctor().getId() != doctor.getId()) {
            return "redirect:/doctor/dashboard";
        }

        // Check if a prescription already exists for this appointment
        Optional<Prescription> existing = prescriptionRepo.findByAppointmentId(appointmentId);

        model.addAttribute("appointment", appt);
        model.addAttribute("doctor", doctor);

        if (existing.isPresent()) {
            model.addAttribute("prescription", existing.get());
            model.addAttribute("isEdit", true);
        } else {
            model.addAttribute("prescription", new Prescription());
            model.addAttribute("isEdit", false);
        }

        return "prescription-form";
    }

    // ── DOCTOR: Save (create) or Update (edit) the prescription ──────────────
    @PostMapping("/doctor/prescription/save")
    public String savePrescription(@RequestParam int appointmentId,
                                   @RequestParam String medicines,
                                   @RequestParam(required = false) String instructions,
                                   @RequestParam(required = false) String diagnosis,
                                   Principal principal) {
        Doctor doctor = doctorService.getDoctorByEmail(principal.getName()).orElseThrow();
        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appt.getDoctor().getId() != doctor.getId()) {
            return "redirect:/doctor/dashboard";
        }

    
        Optional<Prescription> existing = prescriptionRepo.findByAppointmentId(appointmentId);

        Prescription rx = existing.orElseGet(Prescription::new);

     
        if (!existing.isPresent()) {
            rx.setAppointment(appt);
            rx.setPatient(appt.getPatient());
            rx.setDoctor(doctor);
            rx.setPrescribedDate(LocalDate.now());
        }

      
        rx.setMedicines(medicines);
        rx.setInstructions(instructions);
        rx.setDiagnosis(diagnosis);

        prescriptionService.save(rx);

        return "redirect:/doctor/dashboard";
    }

    // ── DOCTOR: View all prescriptions the doctor wrote for a specific patient ─
    @GetMapping("/doctor/patient/{patientId}/prescriptions")
    public String viewPatientPrescriptions(@PathVariable int patientId,
                                           Model model,
                                           Principal principal) {
        Doctor doctor = doctorService.getDoctorByEmail(principal.getName()).orElseThrow();
        Patient patient = patientService.getById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Prescription> prescriptions =
                prescriptionService.getByPatientAndDoctor(patientId, doctor.getId());

        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", patient);
        model.addAttribute("prescriptions", prescriptions);
        return "doctor-patient-prescriptions";
    }

    // ── PATIENT: View own full prescription history ───────────────────────────
    @GetMapping("/patient/prescriptions")
    public String myPrescriptions(Model model, Principal principal) {
        Patient patient = patientService.getPatientByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Prescription> prescriptions = prescriptionService.getByPatient(patient.getId());
        model.addAttribute("patient", patient);
        model.addAttribute("prescriptions", prescriptions);
        return "patient-prescriptions";
    }
}