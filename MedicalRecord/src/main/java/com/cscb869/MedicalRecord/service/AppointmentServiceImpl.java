package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.dto.DoctorDTO;
import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.model.Patient;
import com.cscb869.MedicalRecord.model.Appointment;
import com.cscb869.MedicalRecord.dao.AppointmentDAO;
import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private AppointmentDAO appointmentDAO;
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

        List<AppointmentDTO> appointmentDtos = mapAppointmentsList(appointments);

        return appointmentDtos;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(long id) {
        patientService.findById(id);
        List<Appointment> appointments = new ArrayList<>();
//        appointmentDAO.findAll()
//                .stream()
//                .filter(item -> item.getPatientId() == id)
//                .map(item -> appointments.add(item));

        for (Appointment item : appointmentDAO.findAll()) {
            if (item.getPatientId() == id) {
                appointments.add(item);
            }
        }

        List<AppointmentDTO> appointmentDto = mapAppointmentsList(appointments);

        return appointmentDto;
    }
    @Override
    public List<AppointmentDTO> getAllAppointments(long doctorId, long patientId) {
        //if the following check is not true - NotFoundException will be thrown
        //check for existing patient with this id
        patientService.findById(patientId);
        List<AppointmentDTO> appointments = new LinkedList<>();
        getAllAppointments(patientId)
                .stream()
                .filter(item -> item.getDoctor().getId() == doctorId)
                .map(item -> appointments.add(item));

        return appointments;
    }

    @Override
    public AppointmentDTO getAppointmentDtoById(long id) {
        Appointment appointment = findById(id);
        AppointmentDTO appointmentDto = mapAppointments(appointment);

        return appointmentDto;
    }

    @Override
    public AppointmentDTO createAppointment(Appointment appointment) {
        Appointment appointmentResponse = appointmentDAO.save(appointment);
        AppointmentDTO appointmentResponseDto = mapAppointments(appointmentResponse);

        return appointmentResponseDto;
    }

    @Override
    public AppointmentDTO updateAppointment(Appointment updatedAppointment) {
        Appointment appointmentResponse = appointmentDAO.findById(updatedAppointment.getId())
                .map(appointment -> {
                    appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
                    appointment.setSickLeaveStartDate(updatedAppointment.getSickLeaveStartDate());
                    appointment.setSickLeaveEndDate(updatedAppointment.getSickLeaveEndDate());
                    appointment.setIllness(updatedAppointment.getIllness());
                    appointment.setMedicine(updatedAppointment.getMedicine());
                    return appointmentDAO.saveAndFlush(appointment);
                })
                .orElseThrow(() -> new NotFoundException("Appointment with id " + updatedAppointment.getId() + " is not found."));
        AppointmentDTO appointmentResponseDto = mapAppointments(appointmentResponse);


        return appointmentResponseDto;
    }

    @Override
    public void deleteAppointment(long id) {
        findById(id);

        appointmentDAO.deleteById(id);
    }

    @Override
    public Appointment findById(long id) {
        Appointment appointment = appointmentDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment with id " + id + " is not found."));

        return appointment;
    }

    @Override
    public List<AppointmentDTO> patientsWithGivenIllness(String illness) {
        List<AppointmentDTO> result = new LinkedList<>();
        List<AppointmentDTO> appointments = getAllAppointments();
        for (AppointmentDTO appointment : appointments) {
            if (appointment.getIllness().equals(illness)) result.add(appointment);
        }

        return result;
    }

    @Override
    public List<AppointmentDTO> getAppointmentsToDoctor(long doctorId) {
        List<AppointmentDTO> result = new LinkedList<>();
        List<AppointmentDTO> appointments = getAllAppointments();
        for (AppointmentDTO appointment : appointments) {
            if (appointment.getDoctor().getId() == doctorId) result.add(appointment);
        }

        return result;
    }

    private List<AppointmentDTO> mapAppointmentsList(List<Appointment> appointments) {
        List<AppointmentDTO> result = appointments.stream()
                .map(item -> {
                    Patient patient = patientService.findById(item.getPatientId());
                    PatientDTO patientDto = modelMapper.map(patient, PatientDTO.class);
                    Doctor doctor = doctorService.findById(item.getDoctorId());
                    DoctorDTO doctorDto = modelMapper.map(doctor, DoctorDTO.class);
                    AppointmentDTO appointmentDto = modelMapper.map(item, AppointmentDTO.class);
                    appointmentDto.setPatient(patientDto);
                    appointmentDto.setDoctor(doctorDto);
                    return appointmentDto;
                })
                .collect(toUnmodifiableList());

        return result;
    }

    private AppointmentDTO mapAppointments(Appointment appointment) {
        AppointmentDTO appointmentDto = modelMapper.map(appointment, AppointmentDTO.class);
        Patient patient = patientService.findById(appointment.getPatientId());
        PatientDTO patientDto = modelMapper.map(patient, PatientDTO.class);
        Doctor doctor = doctorService.findById(appointment.getDoctorId());
        DoctorDTO doctorDto = modelMapper.map(doctor, DoctorDTO.class);
        appointmentDto.setPatient(patientDto);
        appointmentDto.setDoctor(doctorDto);

        return appointmentDto;
    }
}
