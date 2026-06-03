package com.example.HospitalManagementSystem.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.HospitalManagementSystem.dao.DoctorTimeSlotRepo;
import com.example.HospitalManagementSystem.entity.DoctorTimeSlot;

@Service
public class DoctorTimeSlotService {

    @Autowired
    DoctorTimeSlotRepo repo;

    public List<DoctorTimeSlot> getSlotsByDoctor(int doctorId) {
        return repo.findByDoctorId(doctorId);
    }

    public void saveSlot(DoctorTimeSlot slot) {
        repo.save(slot);
    }

    public void deleteSlot(int id) {
        repo.deleteById(id);
    }
}