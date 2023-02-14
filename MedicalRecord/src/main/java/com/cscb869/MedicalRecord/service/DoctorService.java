package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.dto.DoctorDTO;
import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.model.Doctor;

import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();
    DoctorDTO getDoctor(long id);
    DoctorDTO createDoctor(Doctor doctor);
    DoctorDTO updateDoctor(Doctor updatedDoctor);
    void deleteDoctor(long id);
    Doctor findById(long id);
    List<AppointmentDTO> getAppointments(long doctorId, long patientId);
    void registerPatientToDoctor(long doctorId, long patientId);
    List<PatientDTO> getPatients(long doctorId);
    void becomeGp(long id);
}
