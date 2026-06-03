package com.example.HospitalManagementSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.HospitalManagementSystem.dao.UserRepo;
import com.example.HospitalManagementSystem.entity.User;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired UserRepo userRepo;
    @Autowired PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepo.findByEmail("admin@medcare.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@medcare.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepo.save(admin);
            System.out.println("✅ Default admin created: admin@medcare.com / admin123");
        }
    }
}