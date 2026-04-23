package com.cscb869.MedicalRecord.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentRequest {
    @JsonProperty("appointment_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @JsonProperty("sick_leave_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sickLeaveStartDate;

    @JsonProperty("sick_leave_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sickLeaveEndDate;

    @JsonProperty("illness")
    private String illness;

    @JsonProperty("medicine")
    private String medicine;

    @JsonProperty("patient_id")
    private long patientId;

    @JsonProperty("doctor_id")
    private long doctorId;
}
