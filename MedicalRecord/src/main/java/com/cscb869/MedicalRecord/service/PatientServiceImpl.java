package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.model.Patient;
import com.cscb869.MedicalRecord.dao.PatientDAO;
import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {
    private PatientDAO patientDAO;
    private final Mapper mapper;
    private final ModelMapper modelMapper;

    public PatientServiceImpl(PatientDAO patientDAO, Mapper mapper, ModelMapper modelMapper) {
        this.patientDAO = patientDAO;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientDAO.findAll();
        if (patients == null) {
            return Collections.emptyList();
        }

        List<PatientDTO> patientsDto = mapper.mapCollection(patients, PatientDTO.class);

        return patientsDto;
    }

    @Override
    public PatientDTO getPatientById(long id) {
        Patient patient = findById(id);

        PatientDTO patientDto = modelMapper.map(patient, PatientDTO.class);

        return patientDto;
    }

    @Override
    public PatientDTO getPatientByEGN(long EGN) {
        Patient patient = patientDAO.findByEGN(EGN);

        PatientDTO patientDto = modelMapper.map(patient, PatientDTO.class);

        return patientDto;
    }

    @Override
    public PatientDTO createPatient(Patient patient) {
        Patient patientResponse = patientDAO.save(patient);
        PatientDTO patientResponseDto = modelMapper.map(patientResponse, PatientDTO.class);

        return patientResponseDto;
    }

    @Override
    public PatientDTO updatePatient(Patient updatedPatient) {
        Patient patientResponse = patientDAO.findById(updatedPatient.getId())
                .map(patient -> {
                    patient.setFirstName(updatedPatient.getFirstName());
                    patient.setLastName(updatedPatient.getLastName());
                    patient.setEGN(updatedPatient.getEGN());
                    patient.setDoctor(updatedPatient.getDoctor());
                    patient.setHasPaidInsurance(updatedPatient.isHasPaidInsurance());
                    return patientDAO.saveAndFlush(patient);
                })
                .orElseThrow(() -> new NotFoundException("Patient with id " + updatedPatient.getId() + " is not found."));
        PatientDTO patientResponseDto = modelMapper.map(patientResponse, PatientDTO.class);

        return patientResponseDto;
    }

    @Override
    public void deletePatient(long id) {
        findById(id);

        patientDAO.deleteById(id);
    }

    @Override
    public Patient findById(long id) {
        Patient patient = patientDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient with id " + id + " is not found."));

        return patient;
    }

    @Override
    public void payInsurance(long id) {
        Patient patient = findById(id);
        patient.setHasPaidInsurance(true);
        patientDAO.saveAndFlush(patient);
    }

    @Override
    public void setDoctor(Patient patient, Doctor doctor) {
        patient.setDoctor(doctor);
        updatePatient(patient);
    }
}
