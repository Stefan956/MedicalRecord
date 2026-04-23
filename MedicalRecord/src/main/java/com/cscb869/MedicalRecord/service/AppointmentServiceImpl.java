package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.dto.AppointmentRequest;
import com.cscb869.MedicalRecord.dto.DoctorDTO;
import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Appointment;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.model.Patient;
import com.cscb869.MedicalRecord.dao.AppointmentDAO;
import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentDAO appointmentDAO;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;
    private final Mapper mapper;
    private final ModelMapper modelMapper;

    public AppointmentServiceImpl(AppointmentDAO appointmentDAO, Mapper mapper, ModelMapper modelMapper) {
        this.appointmentDAO = appointmentDAO;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentDAO.findAll();
        if (appointments == null) {
            return Collections.emptyList();
        }
        return appointments.stream().map(this::mapAppointment).collect(toUnmodifiableList());
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(long patientId) {
        patientService.findById(patientId);
        return appointmentDAO.findAll().stream()
                .filter(a -> a.getPatient() != null && a.getPatient().getId() == patientId)
                .map(this::mapAppointment)
                .collect(toUnmodifiableList());
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(long doctorId, long patientId) {
        patientService.findById(patientId);
        return getAllAppointments(patientId).stream()
                .filter(a -> a.getDoctor() != null && a.getDoctor().getId() == doctorId)
                .collect(toUnmodifiableList());
    }

    @Override
    public AppointmentDTO getAppointmentDtoById(long id) {
        return mapAppointment(findById(id));
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentRequest request) {
        Patient patient = patientService.findById(request.getPatientId());
        Doctor doctor = doctorService.findById(request.getDoctorId());

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setSickLeaveStartDate(request.getSickLeaveStartDate());
        appointment.setSickLeaveEndDate(request.getSickLeaveEndDate());
        appointment.setIllness(request.getIllness());
        appointment.setMedicine(request.getMedicine());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        return mapAppointment(appointmentDAO.save(appointment));
    }

    @Override
    public AppointmentDTO updateAppointment(long id, AppointmentRequest request) {
        Appointment appointment = findById(id);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setSickLeaveStartDate(request.getSickLeaveStartDate());
        appointment.setSickLeaveEndDate(request.getSickLeaveEndDate());
        appointment.setIllness(request.getIllness());
        appointment.setMedicine(request.getMedicine());
        appointment.setPatient(patientService.findById(request.getPatientId()));
        appointment.setDoctor(doctorService.findById(request.getDoctorId()));
        return mapAppointment(appointmentDAO.saveAndFlush(appointment));
    }

    @Override
    public void deleteAppointment(long id) {
        findById(id);
        appointmentDAO.deleteById(id);
    }

    @Override
    public Appointment findById(long id) {
        return appointmentDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment with id " + id + " is not found."));
    }

    @Override
    public List<AppointmentDTO> patientsWithGivenIllness(String illness) {
        List<AppointmentDTO> result = new LinkedList<>();
        for (AppointmentDTO a : getAllAppointments()) {
            if (a.getIllness().equals(illness)) result.add(a);
        }
        return result;
    }

    @Override
    public List<AppointmentDTO> getAppointmentsToDoctor(long doctorId) {
        List<AppointmentDTO> result = new LinkedList<>();
        for (AppointmentDTO a : getAllAppointments()) {
            if (a.getDoctor() != null && a.getDoctor().getId() == doctorId) result.add(a);
        }
        return result;
    }

    private AppointmentDTO mapAppointment(Appointment appointment) {
        AppointmentDTO dto = modelMapper.map(appointment, AppointmentDTO.class);
        if (appointment.getPatient() != null) {
            dto.setPatient(modelMapper.map(appointment.getPatient(), PatientDTO.class));
        }
        if (appointment.getDoctor() != null) {
            dto.setDoctor(modelMapper.map(appointment.getDoctor(), DoctorDTO.class));
        }
        return dto;
    }
}
