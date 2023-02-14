package com.cscb869.MedicalRecord.dao;

import com.cscb869.MedicalRecord.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorDAO extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findById(long id);
}
