package com.example.HospitalManagementSystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.HospitalManagementSystem.dao.DoctorRepo;
import com.example.HospitalManagementSystem.entity.Doctor;

@Service
public class DoctorService {

    @Autowired DoctorRepo repo;

    public List<Doctor> getAllDoctors() { return repo.findAll(); }

    public Optional<Doctor> getDoctorByEmail(String email) {
        return repo.findByUserEmail(email);
    }

    public void saveDoctor(Doctor d) { 
    	    repo.save(d); 
    	}

    public void deleteDoctor(int id) { 
    	    repo.deleteById(id); 
    	}

    public Optional<Doctor> getById(int id) { 
    	     return repo.findById(id); 
    	}
}