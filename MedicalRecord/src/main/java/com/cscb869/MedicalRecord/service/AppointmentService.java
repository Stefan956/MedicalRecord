package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.model.Appointment;

import java.util.List;

public interface AppointmentService {
    List<AppointmentDTO> getAllAppointments();
    List<AppointmentDTO> getAllAppointments(long id);
    List<AppointmentDTO> getAllAppointments(long doctorId, long patientId);
    AppointmentDTO getAppointmentDtoById(long id);
    AppointmentDTO createAppointment(Appointment appointment);
    AppointmentDTO updateAppointment(Appointment updatedAppointment);

    List<AppointmentDTO> patientsWithGivenIllness(String diagnose);
    List<AppointmentDTO> getAppointmentsToDoctor(long doctorId);
    void deleteAppointment(long id);
    Appointment findById(long id);
}
