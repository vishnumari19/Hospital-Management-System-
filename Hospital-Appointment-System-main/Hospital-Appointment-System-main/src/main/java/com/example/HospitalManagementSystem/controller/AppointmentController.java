package com.example.HospitalManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.HospitalManagementSystem.dao.AppointmentRepo;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepo repo;

    @GetMapping("/all")
    public String viewAll(Model model) {
        model.addAttribute("appointments", repo.findAll());
        return "appointments";
    }
}
