package com.example.HospitalManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.HospitalManagementSystem.dao.DoctorRepo;
import com.example.HospitalManagementSystem.dao.PatientRepo;
import com.example.HospitalManagementSystem.dao.UserRepo;
import com.example.HospitalManagementSystem.entity.Doctor;
import com.example.HospitalManagementSystem.entity.Patient;
import com.example.HospitalManagementSystem.entity.User;

@Controller
public class AuthController {

    @Autowired UserRepo userRepo;
    @Autowired DoctorRepo doctorRepo;
    @Autowired PatientRepo patientRepo;
    @Autowired PasswordEncoder encoder;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        @RequestParam(required = false) String registered,
                        @RequestParam(required = false) String pending,
                        Model model) {
        if (error != null)      model.addAttribute("error", "Invalid email or password.");
        if (logout != null)     model.addAttribute("message", "You have been logged out.");
        if (registered != null) model.addAttribute("message", "Account created! Please sign in.");
        if (pending != null)    model.addAttribute("pending", true);
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String gender,
            Model model) {

        // Block admin self-registration
        if ("ROLE_ADMIN".equals(role)) {
            model.addAttribute("error", "Admin accounts cannot be self-registered.");
            return "register";
        }

        if (userRepo.findByEmail(email).isPresent()) {
            model.addAttribute("error", "An account with this email already exists.");
            return "register";
        }

        if (phone != null) {
            phone = phone.trim().replaceAll("[^0-9+]", "");
        }

        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        userRepo.save(u);

        if ("ROLE_DOCTOR".equals(role)) {
            Doctor d = new Doctor();
            d.setName(name);
            d.setSpecialization(specialization);
            d.setPhone(phone);
            d.setEmail(email);
            d.setUser(u);
            d.setApproved(false);
            doctorRepo.save(d);
            return "redirect:/login?pending=true";

        } else if ("ROLE_PATIENT".equals(role)) {
            Patient p = new Patient();
            p.setName(name);
            p.setPhone(phone);
            p.setAge(age != null ? age : 0);
            p.setAddress(address);
            p.setGender(gender);
            p.setUser(u);
            patientRepo.save(p);
        }

        return "redirect:/login?registered=true";
    }
}