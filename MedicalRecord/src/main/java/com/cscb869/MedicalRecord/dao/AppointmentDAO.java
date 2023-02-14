package com.cscb869.MedicalRecord.dao;

import com.cscb869.MedicalRecord.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentDAO extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findById(long id);
}
