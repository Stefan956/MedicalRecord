package com.cscb869.MedicalRecord.controller;

import com.cscb869.MedicalRecord.dto.PatientDTO;
import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Patient;
import com.cscb869.MedicalRecord.service.DoctorService;
import com.cscb869.MedicalRecord.service.PatientService;
import com.cscb869.MedicalRecord.service.AppointmentService;
import com.cscb869.MedicalRecord.commons.paths.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = Constants.API_PATH + Constants.PATIENT_PATH)
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DoctorService doctorService;

    public PatientController() {

    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients();

        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable long id) {
        try {
            PatientDTO patientResponse = patientService.getPatientById(id);

            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/egn/{EGN}")
    public ResponseEntity<?> getPatientByEGN(@PathVariable long EGN) {
        try {
            PatientDTO patientResponse = patientService.getPatientByEGN(EGN);

            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient) {
        try {
            PatientDTO patientResponse = patientService.createPatient(patient);

            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePatient(@Valid @RequestBody Patient patient, @PathVariable long id) {
        try {
            patient.setId(id);
            PatientDTO patientResponse = patientService.updatePatient(patient);

            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable long id) {
        try {
            patientService.deletePatient(id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getMedicalHistory(@PathVariable long id) {
        try {
            List<AppointmentDTO> allAppointments = appointmentService.getAllAppointments(id);

            return new ResponseEntity<>(allAppointments, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/{id}/pay-insurance")
    public ResponseEntity<?> payInsurance(@PathVariable long id) {
        try {
            patientService.payInsurance(id);

            return new ResponseEntity<>("Insurance of patient with id " + id + " is paid successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/{patientId}/register-doctor/{doctorId}")
    public ResponseEntity<?> registerDoctorToPatient(@PathVariable long patientId, @PathVariable long doctorId) {
        try {
            doctorService.registerPatientToDoctor(doctorId, patientId);

            return new ResponseEntity<>("Patient with id " + patientId + " is registered to doctor with id " + doctorId + " successfully.",HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/summary/patients-number-with-illness/{illness}")
    public ResponseEntity<?> getPatientsWithGivenIllness(@PathVariable String illness) {
        try {
            int number = appointmentService.patientsWithGivenIllness(illness).size();

            return new ResponseEntity<>(number,HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
