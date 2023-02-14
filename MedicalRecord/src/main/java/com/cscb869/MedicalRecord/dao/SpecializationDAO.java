package com.cscb869.MedicalRecord.dao;

import com.cscb869.MedicalRecord.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecializationDAO extends JpaRepository<Specialization, Long> {
    Optional<Specialization> findById(long id);
    Specialization findByName(String name);
}
