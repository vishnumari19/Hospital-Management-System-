package com.example.HospitalManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.HospitalManagementSystem.dao.*;
import com.example.HospitalManagementSystem.entity.*;
import com.example.HospitalManagementSystem.service.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired DoctorRepo doctorRepo;
    @Autowired PatientRepo patientRepo;
    @Autowired AppointmentRepo appointmentRepo;
    @Autowired UserRepo userRepo;
    @Autowired DoctorService doctorService;
    @Autowired PatientService patientService;
    @Autowired AppointmentService appointmentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        java.util.List<Doctor> allDoctors = doctorRepo.findAll();
        java.util.List<Doctor> approvedDoctors = allDoctors.stream()
                .filter(Doctor::isApproved).toList();
        java.util.List<Doctor> pendingDoctors = allDoctors.stream()
                .filter(d -> !d.isApproved()).toList();

        model.addAttribute("doctorCount", approvedDoctors.size());
        model.addAttribute("pendingDoctorCount", pendingDoctors.size());
        model.addAttribute("pendingDoctors", pendingDoctors);
        model.addAttribute("doctors", approvedDoctors);
        model.addAttribute("patientCount", patientRepo.count());
        model.addAttribute("appointmentCount", appointmentRepo.count());
        model.addAttribute("patients", patientRepo.findAll());
        model.addAttribute("appointments", appointmentRepo.findAll());
        return "admin-dashboard";
    }

    // Approve doctor
    @PostMapping("/doctor/approve/{id}")
    public String approveDoctor(@PathVariable int id) {
        Doctor d = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        d.setApproved(true);
        doctorRepo.save(d);
        return "redirect:/admin/dashboard";
    }

    // Reject doctor — removes doctor + user account
    @PostMapping("/doctor/reject/{id}")
    public String rejectDoctor(@PathVariable int id) {
        Doctor d = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        User u = d.getUser();
        doctorRepo.delete(d);
        if (u != null) userRepo.delete(u);
        return "redirect:/admin/dashboard";
    }

    // Delete approved doctor
    @PostMapping("/doctor/delete/{id}")
    public String deleteDoctor(@PathVariable int id) {
        Doctor d = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        User u = d.getUser();
        doctorRepo.delete(d);
        if (u != null) userRepo.delete(u);
        return "redirect:/admin/dashboard";
    }

    // Delete patient
    @PostMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable int id) {
        Patient p = patientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        User u = p.getUser();
        patientRepo.delete(p);
        if (u != null) userRepo.delete(u);
        return "redirect:/admin/dashboard";
    }

    // Delete appointment
    @PostMapping("/appointment/delete/{id}")
    public String deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/admin/dashboard";
    }
}