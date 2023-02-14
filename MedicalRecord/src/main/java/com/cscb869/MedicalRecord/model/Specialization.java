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
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="name", nullable = false)
    @JsonProperty("name")
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "doctors_specializations",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id"))
    private List<Doctor> doctors;
}
