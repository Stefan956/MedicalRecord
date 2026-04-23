package com.cscb869.MedicalRecord;

import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import com.cscb869.MedicalRecord.dao.SpecializationDAO;
import com.cscb869.MedicalRecord.dto.SpecializationDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Specialization;
import com.cscb869.MedicalRecord.service.SpecializationServiceImpl;
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
class SpecializationServiceImplTest {

    @Mock private SpecializationDAO specializationDAO;
    @Mock private Mapper mapper;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private SpecializationServiceImpl specializationService;

    // ── helpers ───────────────────────────────────────────────────────────────

    private Specialization spec(long id) {
        Specialization s = new Specialization();
        s.setId(id);
        s.setName("Cardiology");
        return s;
    }

    private SpecializationDTO specDTO(long id) {
        SpecializationDTO dto = new SpecializationDTO();
        dto.setId(id);
        dto.setName("Cardiology");
        return dto;
    }

    // ── getAllSpecializations ─────────────────────────────────────────────────

    @Test
    void getAllSpecializations_returnsAll() {
        List<Specialization> specs = List.of(spec(1L), spec(2L));
        List<SpecializationDTO> dtos = List.of(specDTO(1L), specDTO(2L));
        when(specializationDAO.findAll()).thenReturn(specs);
        when(mapper.mapCollection(specs, SpecializationDTO.class)).thenReturn(dtos);

        List<SpecializationDTO> result = specializationService.getAllSpecializations();

        assertEquals(2, result.size());
    }

    @Test
    void getAllSpecializations_returnsEmptyList_whenDaoReturnsNull() {
        when(specializationDAO.findAll()).thenReturn(null);

        List<SpecializationDTO> result = specializationService.getAllSpecializations();

        assertTrue(result.isEmpty());
    }

    // ── getSpecializationById ─────────────────────────────────────────────────

    @Test
    void getSpecializationById_returnsDTO_whenExists() {
        Specialization s = spec(1L);
        SpecializationDTO dto = specDTO(1L);
        when(specializationDAO.findById(1L)).thenReturn(Optional.of(s));
        when(modelMapper.map(s, SpecializationDTO.class)).thenReturn(dto);

        SpecializationDTO result = specializationService.getSpecializationById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Cardiology", result.getName());
    }

    @Test
    void getSpecializationById_throwsNotFoundException_whenNotExists() {
        when(specializationDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> specializationService.getSpecializationById(99L));
    }

    // ── getSpecializationByName ───────────────────────────────────────────────

    @Test
    void getSpecializationByName_returnsDTO_whenExists() {
        Specialization s = spec(1L);
        SpecializationDTO dto = specDTO(1L);
        when(specializationDAO.findByName("Cardiology")).thenReturn(s);
        when(modelMapper.map(s, SpecializationDTO.class)).thenReturn(dto);

        SpecializationDTO result = specializationService.getSpecializationByName("Cardiology");

        assertEquals("Cardiology", result.getName());
    }

    // ── createSpecialization ──────────────────────────────────────────────────

    @Test
    void createSpecialization_savesAndReturnsDTO() {
        Specialization s = spec(1L);
        SpecializationDTO dto = specDTO(1L);
        when(specializationDAO.save(s)).thenReturn(s);
        when(modelMapper.map(s, SpecializationDTO.class)).thenReturn(dto);

        SpecializationDTO result = specializationService.createSpecialization(s);

        verify(specializationDAO).save(s);
        assertEquals("Cardiology", result.getName());
    }

    // ── updateSpecialization ──────────────────────────────────────────────────

    @Test
    void updateSpecialization_updatesNameAndReturnsDTO() {
        Specialization existing = spec(1L);
        Specialization updated = spec(1L);
        updated.setName("Neurology");
        SpecializationDTO dto = specDTO(1L);
        dto.setName("Neurology");

        when(specializationDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(specializationDAO.saveAndFlush(any())).thenReturn(existing);
        when(modelMapper.map(any(), eq(SpecializationDTO.class))).thenReturn(dto);

        SpecializationDTO result = specializationService.updateSpecialization(updated);

        assertEquals("Neurology", result.getName());
    }

    @Test
    void updateSpecialization_throwsNotFoundException_whenNotExists() {
        Specialization s = spec(99L);
        when(specializationDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> specializationService.updateSpecialization(s));
    }

    // ── deleteSpecialization ──────────────────────────────────────────────────

    @Test
    void deleteSpecialization_deletesSuccessfully() {
        when(specializationDAO.findById(1L)).thenReturn(Optional.of(spec(1L)));

        specializationService.deleteSpecialization(1L);

        verify(specializationDAO).deleteById(1L);
    }

    @Test
    void deleteSpecialization_throwsNotFoundException_whenNotExists() {
        when(specializationDAO.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> specializationService.deleteSpecialization(99L));
    }
}
