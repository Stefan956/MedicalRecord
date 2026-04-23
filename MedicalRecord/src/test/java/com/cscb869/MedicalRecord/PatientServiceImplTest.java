package com.cscb869.MedicalRecord;

import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import com.cscb869.MedicalRecord.dao.PatientDAO;
import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.model.Patient;
import com.cscb869.MedicalRecord.service.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock private PatientDAO patientDAO;
    @Mock private Mapper mapper;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    // ── helpers ──────────────────────────────────────────────────────────────

    private Patient patient(long id) {
        Patient p = new Patient();
        p.setId(id);
        p.setFirstName("Ivan");
        p.setLastName("Petrov");
        p.setEGN(8501010001L);
        return p;
    }

    private PatientDTO patientDTO(long id) {
        PatientDTO dto = new PatientDTO();
        dto.setId(id);
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        return dto;
    }

    // ── getAllPatients ────────────────────────────────────────────────────────

    @Test
    void getAllPatients_returnsAllPatients() {
        List<Patient> patients = List.of(patient(1L), patient(2L));
        List<PatientDTO> dtos = List.of(patientDTO(1L), patientDTO(2L));
        when(patientDAO.findAll()).thenReturn(patients);
        when(mapper.mapCollection(patients, PatientDTO.class)).thenReturn(dtos);

        List<PatientDTO> result = patientService.getAllPatients();

        assertEquals(2, result.size());
    }

    @Test
    void getAllPatients_returnsEmptyList_whenDaoReturnsNull() {
        when(patientDAO.findAll()).thenReturn(null);

        List<PatientDTO> result = patientService.getAllPatients();

        assertTrue(result.isEmpty());
    }

    // ── getPatientById ────────────────────────────────────────────────────────

    @Test
    void getPatientById_returnsPatientDTO_whenExists() {
        Patient p = patient(1L);
        PatientDTO dto = patientDTO(1L);
        when(patientDAO.findById(1L)).thenReturn(Optional.of(p));
        when(modelMapper.map(p, PatientDTO.class)).thenReturn(dto);

        PatientDTO result = patientService.getPatientById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Ivan", result.getFirstName());
    }

    @Test
    void getPatientById_throwsNotFoundException_whenNotExists() {
        when(patientDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> patientService.getPatientById(99L));
    }

    // ── createPatient ─────────────────────────────────────────────────────────

    @Test
    void createPatient_savesAndReturnsDTO() {
        Patient p = patient(1L);
        PatientDTO dto = patientDTO(1L);
        when(patientDAO.save(p)).thenReturn(p);
        when(modelMapper.map(p, PatientDTO.class)).thenReturn(dto);

        PatientDTO result = patientService.createPatient(p);

        verify(patientDAO).save(p);
        assertEquals("Ivan", result.getFirstName());
    }

    // ── updatePatient ─────────────────────────────────────────────────────────

    @Test
    void updatePatient_updatesAndReturnsDTO() {
        Patient existing = patient(1L);
        Patient updated = patient(1L);
        updated.setFirstName("Georgi");
        PatientDTO dto = patientDTO(1L);
        dto.setFirstName("Georgi");

        when(patientDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(patientDAO.saveAndFlush(any())).thenReturn(existing);
        when(modelMapper.map(any(), eq(PatientDTO.class))).thenReturn(dto);

        PatientDTO result = patientService.updatePatient(updated);

        assertEquals("Georgi", result.getFirstName());
    }

    @Test
    void updatePatient_throwsNotFoundException_whenNotExists() {
        Patient p = patient(99L);
        when(patientDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> patientService.updatePatient(p));
    }

    // ── deletePatient ─────────────────────────────────────────────────────────

    @Test
    void deletePatient_deletesSuccessfully() {
        when(patientDAO.findById(1L)).thenReturn(Optional.of(patient(1L)));

        patientService.deletePatient(1L);

        verify(patientDAO).deleteById(1L);
    }

    @Test
    void deletePatient_throwsNotFoundException_whenNotExists() {
        when(patientDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> patientService.deletePatient(99L));
    }

    // ── payInsurance ──────────────────────────────────────────────────────────

    @Test
    void payInsurance_setsInsurancePaidTrue() {
        Patient p = patient(1L);
        p.setHasPaidInsurance(false);
        when(patientDAO.findById(1L)).thenReturn(Optional.of(p));
        when(patientDAO.saveAndFlush(p)).thenReturn(p);

        patientService.payInsurance(1L);

        assertTrue(p.isHasPaidInsurance());
        verify(patientDAO).saveAndFlush(p);
    }

    // ── setDoctor ─────────────────────────────────────────────────────────────

    @Test
    void setDoctor_assignsDoctorToPatient() {
        Patient p = patient(1L);
        Doctor d = new Doctor();
        d.setId(10L);
        when(patientDAO.findById(1L)).thenReturn(Optional.of(p));
        when(patientDAO.saveAndFlush(any())).thenReturn(p);
        when(modelMapper.map(any(), eq(PatientDTO.class))).thenReturn(patientDTO(1L));

        patientService.setDoctor(p, d);

        assertEquals(d, p.getDoctor());
    }
}
