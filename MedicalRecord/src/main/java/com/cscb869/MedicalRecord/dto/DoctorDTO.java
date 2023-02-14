package com.cscb869.MedicalRecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.cscb869.MedicalRecord.model.Patient;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoctorDTO {
    @NotNull
    private long id;
    @NotNull
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @JsonProperty("last_name")
    private String lastName;
    @NotNull
    private String specialization;

    @NotNull
    private boolean isGp;

//    @NotNull
//    private List<Specialization> specializations;

    private List<Patient> patients;
}
