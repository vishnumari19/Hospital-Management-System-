	package com.example.HospitalManagementSystem.service;
	
	import java.util.List;
	import java.util.Optional;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	import com.example.HospitalManagementSystem.dao.PatientRepo;
	import com.example.HospitalManagementSystem.entity.Patient;
	
	@Service
	public class PatientService {
	
	    @Autowired PatientRepo repo;
	
	    public List<Patient> getAllPatients(){ 
	    	       return repo.findAll(); 
	    }
	
	    public Optional<Patient> getPatientByEmail(String email) {
	        return repo.findByUserEmail(email);
	    }
	
	    public void savePatient(Patient p) { 
	    	    repo.save(p); 
	    }
	
	    public void deletePatient(int id) { 
	    	    repo.deleteById(id); 
	    	}
	
	    public Optional<Patient> getById(int id) { 
	      	return repo.findById(id); 
	    	}
	}