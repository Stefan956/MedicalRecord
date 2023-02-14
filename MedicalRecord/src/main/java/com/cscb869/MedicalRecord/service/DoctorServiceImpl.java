package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.dto.DoctorDTO;
import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.model.Patient;
import com.cscb869.MedicalRecord.dao.DoctorDAO;
import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    private DoctorDAO doctorDAO;
    @Autowired
    private PatientService patientService;
    @Autowired
    private AppointmentService appointmentService;
    private final Mapper mapper;
    private final ModelMapper modelMapper;

    public DoctorServiceImpl(DoctorDAO doctorDAO, Mapper mapper, ModelMapper modelMapper) {
        this.doctorDAO = doctorDAO;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorDAO.findAll();
        if(doctors == null) {
            return Collections.emptyList();
        }

        List<DoctorDTO> doctorsDto = mapper.mapCollection(doctors, DoctorDTO.class);

        return doctorsDto;
    }

    @Override
    public DoctorDTO getDoctor(long id) {
        Doctor doctor = findById(id);
        DoctorDTO doctorDto = modelMapper.map(doctor, DoctorDTO.class);

        return doctorDto;
    }

    @Override
    public DoctorDTO createDoctor(Doctor doctor) {
        Doctor doctorResponse = doctorDAO.save(doctor);

        DoctorDTO doctorResponseDto = modelMapper.map(doctorResponse, DoctorDTO.class);

        return doctorResponseDto;
    }

    @Override
    public DoctorDTO updateDoctor(Doctor updatedDoctor) {
        Doctor doctorResponse = doctorDAO.findById(updatedDoctor.getId())
                .map(doctor -> {
                    doctor.setFirstName(updatedDoctor.getFirstName());
                    doctor.setLastName(updatedDoctor.getLastName());
//                    doctor.setSpecialization(updatedDoctor.getSpecialization());
                    doctor.setGp(updatedDoctor.isGp());
                    doctor.setPatients(updatedDoctor.getPatients());
                    return doctorDAO.saveAndFlush(doctor);
                })
                .orElseThrow(() -> new NotFoundException("Doctor with id " + updatedDoctor.getId() + " is not found."));

        DoctorDTO doctorResponseDto = modelMapper.map(doctorResponse, DoctorDTO.class);

        return doctorResponseDto;
    }

    @Override
    public void deleteDoctor(long id) {
        findById(id);

        doctorDAO.deleteById(id);
    }

    @Override
    public Doctor findById(long id) {
        Doctor doctor = doctorDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor with id " + id + " is not found."));

        return doctor;
    }

    @Override
    public List<AppointmentDTO> getAppointments(long doctorId, long patientId) {
        //if the following check is not true - NotFoundException will be thrown
        //check for existing doctor with this id
        findById(doctorId);

        List<AppointmentDTO> appointments = appointmentService.getAllAppointments(doctorId, patientId);

        return appointments;
    }

    @Override
    public void registerPatientToDoctor(long doctorId, long patientId) {
        Doctor doctor = findById(doctorId);
        Patient patient = patientService.findById(patientId);
        doctor.getPatients().add(patient);
        updateDoctor(doctor);
        patientService.setDoctor(patient, doctor);
    }

    @Override
    public List<PatientDTO> getPatients(long doctorId) {
        Doctor doctor = findById(doctorId);
        List<PatientDTO> result = mapper.mapCollection(doctor.getPatients(), PatientDTO.class);

        return result;
    }

    @Override
    public void becomeGp(long id) {
        Doctor doctor = findById(id);
        doctor.setGp(true);
        doctorDAO.saveAndFlush(doctor);
    }
}
