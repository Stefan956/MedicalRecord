package com.cscb869.MedicalRecord.service;

import com.cscb869.MedicalRecord.dto.SpecializationDTO;
import com.cscb869.MedicalRecord.model.Specialization;

import java.util.List;

public interface SpecializationService {
    public List<SpecializationDTO> getAllSpecializations();
    public SpecializationDTO getSpecializationById(long id);
    public SpecializationDTO getSpecializationByName(String name);
    public SpecializationDTO createSpecialization(Specialization specialization);
    public SpecializationDTO updateSpecialization(Specialization updatedSpecialization);
    public void deleteSpecialization(long id);
    public Specialization findById(long id);

}
