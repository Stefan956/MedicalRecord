package com.cscb869.MedicalRecord.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name", nullable = false)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @JsonProperty("last_name")
    private String lastName;

    @JsonIgnore
//    @ManyToMany(fetch = EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "doctor_specializations",
//            joinColumns = @JoinColumn(name = "specialization_id"),
//            inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    @ManyToMany(mappedBy = "doctors")
    private List<Specialization> specializations;

    @JsonProperty("doctor_is_gp")
    private boolean isGp = false;

    @OneToMany(mappedBy = "doctor",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JsonProperty("registered_patients")
    private List<Patient> patients;
}
