package com.cscb869.MedicalRecord.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonProperty("appointment_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate appointmentDate;
    @JsonProperty("sick_leave_start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate sickLeaveStartDate;
    @JsonProperty("sick_leave_end_date")
    @JsonFormat(pattern="yyyy-MM-dd")
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
