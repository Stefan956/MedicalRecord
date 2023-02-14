package com.cscb869.MedicalRecord.dto;

import com.cscb869.MedicalRecord.model.Doctor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecializationDTO {
    @NotNull
    private long id;

    @NotNull
    private String name;

//    private List<Doctor> doctors;
}