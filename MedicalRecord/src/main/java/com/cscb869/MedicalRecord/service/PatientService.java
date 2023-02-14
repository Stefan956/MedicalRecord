package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.model.Patient;

import java.util.List;

public interface PatientService {
    List<PatientDTO> getAllPatients();
    PatientDTO getPatientById(long id);
    PatientDTO getPatientByEGN(long EGN);
    PatientDTO createPatient(Patient patient);
    PatientDTO updatePatient(Patient updatedPatient);
    void deletePatient(long id);
    Patient findById(long id);
    void payInsurance(long id);
    void setDoctor(Patient patient, Doctor doctor);
}
