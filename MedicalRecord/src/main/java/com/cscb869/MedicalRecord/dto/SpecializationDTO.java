package com.cscb869.MedicalRecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecializationDTO {
    @NotNull
    private long id;
    @NotNull
    @JsonProperty("name")
    private String name;
}
