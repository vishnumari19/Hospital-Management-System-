package com.example.HospitalManagementSystem.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_time_slots")
public class DoctorTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private String dayOfWeek;   // e.g. "MONDAY", "TUESDAY"
    private LocalTime startTime;
    private LocalTime endTime;
	public DoctorTimeSlot() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DoctorTimeSlot(Doctor doctor, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
		super();
		this.doctor = doctor;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
   
}