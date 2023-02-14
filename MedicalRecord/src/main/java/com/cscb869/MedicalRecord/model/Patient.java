package com.cscb869.MedicalRecord.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="first_name", nullable = false)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name="last_name", nullable = false)
    @JsonProperty("last_name")
    private String lastName;
    @Column(name="egn", nullable = false)
    @JsonProperty("egn")
    private long EGN;
    @ManyToOne
    @JoinColumn(name="doctor_id")
    @JsonIgnore
    private Doctor doctor;
    @JsonProperty("has_paid_insurance")
    private boolean hasPaidInsurance = false;
}
