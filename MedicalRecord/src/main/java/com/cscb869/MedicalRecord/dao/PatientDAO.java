package com.cscb869.MedicalRecord.dao;

import com.cscb869.MedicalRecord.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientDAO extends JpaRepository<Patient, Long> {
    Optional<Patient> findById(long id);
    //Why for findByRNG Optional<Patient> throws an error, while in findById it doesn't?
    Patient findByEGN(long EGN);
}
