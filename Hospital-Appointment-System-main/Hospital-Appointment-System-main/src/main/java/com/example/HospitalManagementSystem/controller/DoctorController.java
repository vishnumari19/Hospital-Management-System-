package com.example.HospitalManagementSystem.controller;

import java.security.Principal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.HospitalManagementSystem.dao.PrescriptionRepo;
import com.example.HospitalManagementSystem.entity.*;
import com.example.HospitalManagementSystem.service.*;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired DoctorService         doctorService;
    @Autowired AppointmentService    appointmentService;
    @Autowired DoctorTimeSlotService slotService;
    @Autowired PrescriptionRepo      prescriptionRepo;   // ← NEW

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Doctor doctor = doctorService.getDoctorByEmail(principal.getName()).orElse(null);

        if (doctor == null) {
            model.addAttribute("error", "Doctor profile not found.");
            model.addAttribute("appointments", java.util.Collections.emptyList());
            model.addAttribute("totalCount", 0);
            model.addAttribute("pendingCount", 0);
            model.addAttribute("approvedCount", 0);
            model.addAttribute("prescriptionMap", new HashMap<>());
        } else {
            List<Appointment> appts = appointmentService.getDoctorAppointments(doctor.getId());

            // Build a map of appointmentId → true for every APPROVED appointment
            // that already has a prescription written. Used by the dashboard HTML
            // to toggle between "Write Prescription" and "Edit Prescription".
            Map<Integer, Boolean> prescriptionMap = new HashMap<>();
            for (Appointment a : appts) {
                if (a.getStatus() == AppointmentStatus.APPROVED) {
                    prescriptionRepo.findByAppointmentId(a.getId())
                            .ifPresent(rx -> prescriptionMap.put(a.getId(), true));
                }
            }

            model.addAttribute("doctor", doctor);
            model.addAttribute("appointments", appts);
            model.addAttribute("prescriptionMap", prescriptionMap);
            model.addAttribute("totalCount", appts.size());
            model.addAttribute("pendingCount",
                    appts.stream().filter(a -> a.getStatus() == AppointmentStatus.PENDING).count());
            model.addAttribute("approvedCount",
                    appts.stream().filter(a -> a.getStatus() == AppointmentStatus.APPROVED).count());
        }
        return "doctor-dashboard";
    }

    @PostMapping("/appointment/{id}/status")
    public String updateStatus(@PathVariable int id, @RequestParam String status) {
        appointmentService.updateStatus(id, AppointmentStatus.valueOf(status));
        return "redirect:/doctor/dashboard";
    }

    @GetMapping("/slots")
    public String manageSlots(Model model, Principal principal) {
        Doctor doctor = doctorService.getDoctorByEmail(principal.getName()).orElseThrow();
        model.addAttribute("doctor", doctor);
        model.addAttribute("slots", slotService.getSlotsByDoctor(doctor.getId()));
        return "doctor-slots";
    }

    @PostMapping("/slots/add")
    public String addSlot(@RequestParam String dayOfWeek,
                          @RequestParam String startTime,
                          @RequestParam String endTime,
                          Principal principal) {
        Doctor doctor = doctorService.getDoctorByEmail(principal.getName()).orElseThrow();
        DoctorTimeSlot slot = new DoctorTimeSlot();
        slot.setDoctor(doctor);
        slot.setDayOfWeek(dayOfWeek.toUpperCase());
        slot.setStartTime(LocalTime.parse(startTime));
        slot.setEndTime(LocalTime.parse(endTime));
        slotService.saveSlot(slot);
        return "redirect:/doctor/slots";
    }

    @PostMapping("/slots/delete/{id}")
    public String deleteSlot(@PathVariable int id) {
        slotService.deleteSlot(id);
        return "redirect:/doctor/slots";
    }
}