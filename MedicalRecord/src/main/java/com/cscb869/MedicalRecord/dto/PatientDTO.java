package com.cscb869.MedicalRecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.cscb869.MedicalRecord.model.Doctor;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {
    @NotNull
    private long id;
    @NotNull
    @JsonProperty("first_name")
    private String firstName;
    @NotNull
    @JsonProperty("last_name")
    private String lastName;
    private Doctor doctor;
    @JsonProperty("has_paid_insurance")
    private boolean hasPaidInsurance;
}
