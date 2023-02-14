package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import com.cscb869.MedicalRecord.dao.SpecializationDAO;
import com.cscb869.MedicalRecord.dto.SpecializationDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Specialization;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class SpecializationServiceImpl implements SpecializationService{

    private SpecializationDAO specializationDAO;
    private final Mapper mapper;
    private final ModelMapper modelMapper;

    public SpecializationServiceImpl(SpecializationDAO specializationDAO, Mapper mapper, ModelMapper modelMapper) {
        this.specializationDAO = specializationDAO;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    public List<SpecializationDTO> getAllSpecializations() {
        List<Specialization> specializations = specializationDAO.findAll();
        if (specializations == null) {
            return Collections.emptyList();
        }

        List<SpecializationDTO> specializationDTOList = mapper.mapCollection(specializations, SpecializationDTO.class);

        return specializationDTOList;
    }
    public SpecializationDTO getSpecializationById(long id) {
        Specialization specialization = findById(id);

        SpecializationDTO specializationDTO = modelMapper.map(specialization, SpecializationDTO.class);

        return specializationDTO;
    }
    public SpecializationDTO getSpecializationByName(String name) {
        Specialization specialization = specializationDAO.findByName(name);

        SpecializationDTO specializationDTO = modelMapper.map(specialization, SpecializationDTO.class);

        return specializationDTO;
    }
    public SpecializationDTO createSpecialization(Specialization specialization) {
        Specialization specializationResponse = specializationDAO.save(specialization);

        SpecializationDTO specializationResponseDto = modelMapper.map(specializationResponse, SpecializationDTO.class);

        return specializationResponseDto;
    }
    public SpecializationDTO updateSpecialization(Specialization updatedSpecialization) {
        Specialization specializationResponse = specializationDAO.findById(updatedSpecialization.getId())
                .map(specialization -> {
                    specialization.setName(updatedSpecialization.getName());
//                    specialization.setDoctors(updatedSpecialization.getDoctors());
                    return specializationDAO.saveAndFlush(specialization);
                })
                .orElseThrow(() -> new NotFoundException("Specialization with id " + updatedSpecialization.getId() + " is not found."));
        SpecializationDTO specializationResponseDto = modelMapper.map(specializationResponse, SpecializationDTO.class);

        return specializationResponseDto;
    }
    public void deleteSpecialization(long id) {
        findById(id);

        specializationDAO.deleteById(id);
    }
    public Specialization findById(long id) {
        Specialization specialization = specializationDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialization with id " + id + " is not found."));

        return specialization;
    }
}
