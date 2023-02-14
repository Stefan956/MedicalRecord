package com.cscb869.MedicalRecord.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentDTO {
    @NotNull
    private long id;
    @NotNull
    @JsonProperty("appointment_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate appointmentDate;
    @JsonProperty("sick_leave_start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate sickLeaveStartDate;
    @JsonProperty("sick_leave_end_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate sickLeaveEndDate;
    @NotNull
    @JsonProperty("illness")
    private String illness;
    @NotNull
    @JsonProperty("medicine")
    private String medicine;
    private PatientDTO patient;
    private DoctorDTO doctor;
}
