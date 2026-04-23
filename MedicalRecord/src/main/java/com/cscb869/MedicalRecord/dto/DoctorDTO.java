package com.cscb869.MedicalRecord.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonProperty("doctor_is_gp")
    private boolean isGp;

    @JsonProperty("registered_patients")
    @JsonIgnore
    private List<Patient> patients;
}
