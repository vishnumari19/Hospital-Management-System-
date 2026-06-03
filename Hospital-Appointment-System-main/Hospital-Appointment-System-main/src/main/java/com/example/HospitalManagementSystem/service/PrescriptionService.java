package com.example.HospitalManagementSystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.HospitalManagementSystem.dao.PrescriptionRepo;
import com.example.HospitalManagementSystem.entity.Prescription;

@Service
public class PrescriptionService {

    @Autowired PrescriptionRepo repo;

    public void save(Prescription p) { repo.save(p); }

    public Optional<Prescription> getById(int id) { return repo.findById(id); }

    public List<Prescription> getByPatient(int patientId) {
        return repo.findByPatientIdOrderByPrescribedDateDesc(patientId);
    }


    public List<Prescription> getByPatientAndDoctor(int patientId, int doctorId) {
        return repo.findByPatientIdAndDoctorIdOrderByPrescribedDateDesc(patientId, doctorId);
    }

    public List<Prescription> getByDoctor(int doctorId) {
        return repo.findByDoctorIdOrderByPrescribedDateDesc(doctorId);
    }
}