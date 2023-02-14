package com.cscb869.MedicalRecord.controller;

import com.cscb869.MedicalRecord.dto.DoctorDTO;
import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.service.DoctorService;
import com.cscb869.MedicalRecord.commons.paths.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = Constants.API_PATH + Constants.DOCTOR_PATH)
public class DoctorController {
    private DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();

        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable long id) {
        try {
            DoctorDTO doctorResponse = doctorService.getDoctor(id);

            return new ResponseEntity<>(doctorResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody Doctor doctor) {
        try {
            DoctorDTO doctorResponse = doctorService.createDoctor(doctor);

            return new ResponseEntity<>(doctorResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDoctor(@Valid @RequestBody Doctor doctor, @PathVariable long id) {
        try {
            doctor.setId(id);
            DoctorDTO doctorResponse = doctorService.updateDoctor(doctor);

            return new ResponseEntity<>(doctorResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable long id) {
        try {
            doctorService.deleteDoctor(id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{doctorId}/patient-history/{patientId}")
    public ResponseEntity<?> getPatientsAppointments(@PathVariable long doctorId, @PathVariable long patientId) {
        try {
            List<AppointmentDTO> sickSheetsOfPatient = doctorService.getAppointments(doctorId, patientId);

            return new ResponseEntity<>(sickSheetsOfPatient, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/{doctorId}/registered-patients")
    public ResponseEntity<?> getPatientsOfDoctor(@PathVariable long doctorId) {
        try {
            int number = doctorService.getPatients(doctorId).size();

            return new ResponseEntity<>(number, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/{id}/become-gp")
    public ResponseEntity<?> payInsurance(@PathVariable long id) {
        try {
            doctorService.becomeGp(id);

            return new ResponseEntity<>("Doctor with id " + id + " became GP.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
