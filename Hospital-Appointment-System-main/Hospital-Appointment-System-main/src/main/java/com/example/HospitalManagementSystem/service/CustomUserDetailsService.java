package com.example.HospitalManagementSystem.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.example.HospitalManagementSystem.dao.DoctorRepo;
import com.example.HospitalManagementSystem.dao.UserRepo;
import com.example.HospitalManagementSystem.entity.Doctor;
import com.example.HospitalManagementSystem.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired UserRepo repo;
    @Autowired DoctorRepo doctorRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user: " + email));

        // Block unapproved doctors from logging in
        if ("ROLE_DOCTOR".equals(user.getRole())) {
            Doctor doctor = doctorRepo.findByUserEmail(email).orElse(null);
            if (doctor == null || !doctor.isApproved()) {
                throw new UsernameNotFoundException("Doctor account not yet approved by admin.");
            }
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
                .build();
    }
}