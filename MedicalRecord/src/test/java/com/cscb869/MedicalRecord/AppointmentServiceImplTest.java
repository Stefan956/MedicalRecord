package com.cscb869.MedicalRecord;

import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import com.cscb869.MedicalRecord.dao.AppointmentDAO;
import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.dto.AppointmentRequest;
import com.cscb869.MedicalRecord.dto.DoctorDTO;
import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Appointment;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.model.Patient;
import com.cscb869.MedicalRecord.service.AppointmentServiceImpl;
import com.cscb869.MedicalRecord.service.DoctorService;
import com.cscb869.MedicalRecord.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock private AppointmentDAO appointmentDAO;
    @Mock private PatientService patientService;
    @Mock private DoctorService doctorService;
    @Mock private Mapper mapper;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    // ── helpers ───────────────────────────────────────────────────────────────

    private Patient patient(long id) {
        Patient p = new Patient();
        p.setId(id);
        p.setFirstName("Ivan");
        p.setLastName("Petrov");
        return p;
    }

    private Doctor doctor(long id) {
        Doctor d = new Doctor();
        d.setId(id);
        d.setFirstName("Maria");
        d.setLastName("Georgieva");
        return d;
    }

    private Appointment appointment(long id, Patient patient, Doctor doctor) {
        Appointment a = new Appointment();
        a.setId(id);
        a.setAppointmentDate(LocalDate.of(2024, 3, 15));
        a.setIllness("Influenza");
        a.setMedicine("Paracetamol");
        a.setPatient(patient);
        a.setDoctor(doctor);
        return a;
    }

    private AppointmentDTO appointmentDTO(long id, Patient patient, Doctor doctor) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(id);
        dto.setIllness("Influenza");
        dto.setMedicine("Paracetamol");
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        dto.setPatient(patientDTO);
        dto.setDoctor(doctorDTO);
        return dto;
    }

    private AppointmentRequest request(long patientId, long doctorId) {
        AppointmentRequest req = new AppointmentRequest();
        req.setAppointmentDate(LocalDate.of(2024, 3, 15));
        req.setIllness("Influenza");
        req.setMedicine("Paracetamol");
        req.setPatientId(patientId);
        req.setDoctorId(doctorId);
        return req;
    }

    private void stubModelMapper(Appointment a, Patient p, Doctor d, AppointmentDTO dto) {
        when(modelMapper.map(a, AppointmentDTO.class)).thenReturn(dto);
        when(modelMapper.map(p, PatientDTO.class)).thenReturn(dto.getPatient());
        when(modelMapper.map(d, DoctorDTO.class)).thenReturn(dto.getDoctor());
    }

    // ── getAllAppointments ────────────────────────────────────────────────────

    @Test
    void getAllAppointments_returnsAllAppointments() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        Appointment a = appointment(1L, p, d);
        AppointmentDTO dto = appointmentDTO(1L, p, d);

        when(appointmentDAO.findAll()).thenReturn(List.of(a));
        stubModelMapper(a, p, d, dto);

        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        assertEquals(1, result.size());
        assertEquals("Influenza", result.get(0).getIllness());
    }

    @Test
    void getAllAppointments_returnsEmptyList_whenDaoReturnsNull() {
        when(appointmentDAO.findAll()).thenReturn(null);

        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        assertTrue(result.isEmpty());
    }

    // ── getAllAppointments by patientId ───────────────────────────────────────

    @Test
    void getAllAppointments_byPatientId_returnsFilteredList() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        Appointment a1 = appointment(1L, p, d);
        Appointment a2 = appointment(2L, patient(99L), d);
        AppointmentDTO dto = appointmentDTO(1L, p, d);

        when(patientService.findById(1L)).thenReturn(p);
        when(appointmentDAO.findAll()).thenReturn(List.of(a1, a2));
        stubModelMapper(a1, p, d, dto);

        List<AppointmentDTO> result = appointmentService.getAllAppointments(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    // ── getAppointmentDtoById ─────────────────────────────────────────────────

    @Test
    void getAppointmentDtoById_returnsDTO_whenExists() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        Appointment a = appointment(1L, p, d);
        AppointmentDTO dto = appointmentDTO(1L, p, d);

        when(appointmentDAO.findById(1L)).thenReturn(Optional.of(a));
        stubModelMapper(a, p, d, dto);

        AppointmentDTO result = appointmentService.getAppointmentDtoById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getAppointmentDtoById_throwsNotFoundException_whenNotExists() {
        when(appointmentDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> appointmentService.getAppointmentDtoById(99L));
    }

    // ── createAppointment ─────────────────────────────────────────────────────

    @Test
    void createAppointment_savesAndReturnsDTO() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        AppointmentRequest req = request(1L, 2L);
        Appointment saved = appointment(1L, p, d);
        AppointmentDTO dto = appointmentDTO(1L, p, d);

        when(patientService.findById(1L)).thenReturn(p);
        when(doctorService.findById(2L)).thenReturn(d);
        when(appointmentDAO.save(any())).thenReturn(saved);
        stubModelMapper(saved, p, d, dto);

        AppointmentDTO result = appointmentService.createAppointment(req);

        verify(appointmentDAO).save(any());
        assertEquals("Influenza", result.getIllness());
    }

    // ── updateAppointment ─────────────────────────────────────────────────────

    @Test
    void updateAppointment_updatesAndReturnsDTO() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        Appointment existing = appointment(1L, p, d);
        AppointmentRequest req = request(1L, 2L);
        req.setIllness("Bronchitis");
        AppointmentDTO dto = appointmentDTO(1L, p, d);
        dto.setIllness("Bronchitis");

        when(appointmentDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(patientService.findById(1L)).thenReturn(p);
        when(doctorService.findById(2L)).thenReturn(d);
        when(appointmentDAO.saveAndFlush(any())).thenReturn(existing);
        stubModelMapper(existing, p, d, dto);

        AppointmentDTO result = appointmentService.updateAppointment(1L, req);

        assertEquals("Bronchitis", result.getIllness());
        verify(appointmentDAO).saveAndFlush(existing);
    }

    @Test
    void updateAppointment_throwsNotFoundException_whenNotExists() {
        when(appointmentDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> appointmentService.updateAppointment(99L, request(1L, 2L)));
    }

    // ── deleteAppointment ─────────────────────────────────────────────────────

    @Test
    void deleteAppointment_deletesSuccessfully() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        when(appointmentDAO.findById(1L)).thenReturn(Optional.of(appointment(1L, p, d)));

        appointmentService.deleteAppointment(1L);

        verify(appointmentDAO).deleteById(1L);
    }

    @Test
    void deleteAppointment_throwsNotFoundException_whenNotExists() {
        when(appointmentDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> appointmentService.deleteAppointment(99L));
    }

    // ── getAppointmentsToDoctor ───────────────────────────────────────────────

    @Test
    void getAppointmentsToDoctor_returnsAppointmentsForDoctor() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        Appointment a = appointment(1L, p, d);
        AppointmentDTO dto = appointmentDTO(1L, p, d);

        when(appointmentDAO.findAll()).thenReturn(List.of(a));
        stubModelMapper(a, p, d, dto);

        List<AppointmentDTO> result = appointmentService.getAppointmentsToDoctor(2L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getDoctor().getId());
    }

    @Test
    void getAppointmentsToDoctor_returnsEmptyList_whenNoneMatch() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        Appointment a = appointment(1L, p, d);
        AppointmentDTO dto = appointmentDTO(1L, p, d);

        when(appointmentDAO.findAll()).thenReturn(List.of(a));
        stubModelMapper(a, p, d, dto);

        List<AppointmentDTO> result = appointmentService.getAppointmentsToDoctor(99L);

        assertTrue(result.isEmpty());
    }

    // ── patientsWithGivenIllness ──────────────────────────────────────────────

    @Test
    void patientsWithGivenIllness_returnsMatchingAppointments() {
        Patient p = patient(1L);
        Doctor d = doctor(2L);
        Appointment a1 = appointment(1L, p, d);
        Appointment a2 = appointment(2L, p, d);
        a2.setIllness("Bronchitis");
        AppointmentDTO dto1 = appointmentDTO(1L, p, d);
        AppointmentDTO dto2 = appointmentDTO(2L, p, d);
        dto2.setIllness("Bronchitis");

        when(appointmentDAO.findAll()).thenReturn(List.of(a1, a2));
        when(modelMapper.map(a1, AppointmentDTO.class)).thenReturn(dto1);
        when(modelMapper.map(p, PatientDTO.class)).thenReturn(dto1.getPatient());
        when(modelMapper.map(d, DoctorDTO.class)).thenReturn(dto1.getDoctor());
        when(modelMapper.map(a2, AppointmentDTO.class)).thenReturn(dto2);

        List<AppointmentDTO> result = appointmentService.patientsWithGivenIllness("Influenza");

        assertEquals(1, result.size());
        assertEquals("Influenza", result.get(0).getIllness());
    }
}
