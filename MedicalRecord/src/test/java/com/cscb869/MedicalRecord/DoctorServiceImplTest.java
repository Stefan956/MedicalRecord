package com.cscb869.MedicalRecord;

import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import com.cscb869.MedicalRecord.dao.DoctorDAO;
import com.cscb869.MedicalRecord.dto.DoctorDTO;
import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.model.Patient;
import com.cscb869.MedicalRecord.service.AppointmentService;
import com.cscb869.MedicalRecord.service.DoctorServiceImpl;
import com.cscb869.MedicalRecord.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock private DoctorDAO doctorDAO;
    @Mock private PatientService patientService;
    @Mock private AppointmentService appointmentService;
    @Mock private Mapper mapper;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    // ── helpers ───────────────────────────────────────────────────────────────

    private Doctor doctor(long id) {
        Doctor d = new Doctor();
        d.setId(id);
        d.setFirstName("Maria");
        d.setLastName("Georgieva");
        d.setGp(false);
        d.setPatients(new ArrayList<>());
        return d;
    }

    private DoctorDTO doctorDTO(long id) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(id);
        dto.setFirstName("Maria");
        dto.setLastName("Georgieva");
        return dto;
    }

    // ── getAllDoctors ─────────────────────────────────────────────────────────

    @Test
    void getAllDoctors_returnsAllDoctors() {
        List<Doctor> doctors = List.of(doctor(1L), doctor(2L));
        List<DoctorDTO> dtos = List.of(doctorDTO(1L), doctorDTO(2L));
        when(doctorDAO.findAll()).thenReturn(doctors);
        when(mapper.mapCollection(doctors, DoctorDTO.class)).thenReturn(dtos);

        List<DoctorDTO> result = doctorService.getAllDoctors();

        assertEquals(2, result.size());
    }

    @Test
    void getAllDoctors_returnsEmptyList_whenDaoReturnsNull() {
        when(doctorDAO.findAll()).thenReturn(null);

        List<DoctorDTO> result = doctorService.getAllDoctors();

        assertTrue(result.isEmpty());
    }

    // ── getDoctor ─────────────────────────────────────────────────────────────

    @Test
    void getDoctor_returnsDoctorDTO_whenExists() {
        Doctor d = doctor(1L);
        DoctorDTO dto = doctorDTO(1L);
        when(doctorDAO.findById(1L)).thenReturn(Optional.of(d));
        when(modelMapper.map(d, DoctorDTO.class)).thenReturn(dto);

        DoctorDTO result = doctorService.getDoctor(1L);

        assertEquals(1L, result.getId());
        assertEquals("Maria", result.getFirstName());
    }

    @Test
    void getDoctor_throwsNotFoundException_whenNotExists() {
        when(doctorDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> doctorService.getDoctor(99L));
    }

    // ── createDoctor ──────────────────────────────────────────────────────────

    @Test
    void createDoctor_savesAndReturnsDTO() {
        Doctor d = doctor(1L);
        DoctorDTO dto = doctorDTO(1L);
        when(doctorDAO.save(d)).thenReturn(d);
        when(modelMapper.map(d, DoctorDTO.class)).thenReturn(dto);

        DoctorDTO result = doctorService.createDoctor(d);

        verify(doctorDAO).save(d);
        assertEquals("Maria", result.getFirstName());
    }

    // ── updateDoctor ──────────────────────────────────────────────────────────

    @Test
    void updateDoctor_updatesAndReturnsDTO() {
        Doctor existing = doctor(1L);
        Doctor updated = doctor(1L);
        updated.setFirstName("Petya");
        DoctorDTO dto = doctorDTO(1L);
        dto.setFirstName("Petya");

        when(doctorDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(doctorDAO.saveAndFlush(any())).thenReturn(existing);
        when(modelMapper.map(any(), eq(DoctorDTO.class))).thenReturn(dto);

        DoctorDTO result = doctorService.updateDoctor(updated);

        assertEquals("Petya", result.getFirstName());
    }

    @Test
    void updateDoctor_throwsNotFoundException_whenNotExists() {
        Doctor d = doctor(99L);
        when(doctorDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> doctorService.updateDoctor(d));
    }

    // ── deleteDoctor ──────────────────────────────────────────────────────────

    @Test
    void deleteDoctor_deletesSuccessfully() {
        when(doctorDAO.findById(1L)).thenReturn(Optional.of(doctor(1L)));

        doctorService.deleteDoctor(1L);

        verify(doctorDAO).deleteById(1L);
    }

    @Test
    void deleteDoctor_throwsNotFoundException_whenNotExists() {
        when(doctorDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> doctorService.deleteDoctor(99L));
    }

    // ── becomeGp ─────────────────────────────────────────────────────────────

    @Test
    void becomeGp_setsDoctorAsGp() {
        Doctor d = doctor(1L);
        when(doctorDAO.findById(1L)).thenReturn(Optional.of(d));
        when(doctorDAO.saveAndFlush(d)).thenReturn(d);

        doctorService.becomeGp(1L);

        assertTrue(d.isGp());
        verify(doctorDAO).saveAndFlush(d);
    }

    // ── registerPatientToDoctor ───────────────────────────────────────────────

    @Test
    void registerPatientToDoctor_addsPatientToDoctor() {
        Doctor d = doctor(1L);
        Patient p = new Patient();
        p.setId(5L);
        p.setFirstName("Ivan");
        p.setLastName("Petrov");

        when(doctorDAO.findById(1L)).thenReturn(Optional.of(d));
        when(patientService.findById(5L)).thenReturn(p);
        when(doctorDAO.saveAndFlush(any())).thenReturn(d);
        when(modelMapper.map(any(), eq(DoctorDTO.class))).thenReturn(doctorDTO(1L));

        doctorService.registerPatientToDoctor(1L, 5L);

        assertTrue(d.getPatients().contains(p));
    }

    // ── getPatients ───────────────────────────────────────────────────────────

    @Test
    void getPatients_returnsPatientDTOList() {
        Doctor d = doctor(1L);
        Patient p = new Patient();
        p.setId(5L);
        d.getPatients().add(p);
        PatientDTO dto = new PatientDTO();
        dto.setId(5L);

        when(doctorDAO.findById(1L)).thenReturn(Optional.of(d));
        when(mapper.mapCollection(d.getPatients(), PatientDTO.class)).thenReturn(List.of(dto));

        List<PatientDTO> result = doctorService.getPatients(1L);

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getId());
    }
}
