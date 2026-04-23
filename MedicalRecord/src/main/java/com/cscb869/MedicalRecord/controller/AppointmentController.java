package com.cscb869.MedicalRecord.controller;

import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.dto.AppointmentRequest;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.service.AppointmentService;
import com.cscb869.MedicalRecord.commons.paths.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = Constants.API_PATH + Constants.APPOINTMENT_PATH)
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllAppointments() {
        List<AppointmentDTO> allAppointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(allAppointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(appointmentService.getAppointmentDtoById(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        try {
            return new ResponseEntity<>(appointmentService.createAppointment(request), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(@RequestBody AppointmentRequest request, @PathVariable long id) {
        try {
            return new ResponseEntity<>(appointmentService.updateAppointment(id, request), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable long id) {
        try {
            appointmentService.deleteAppointment(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{doctorId}/visitations")
    public ResponseEntity<?> getAppointmentsToGivenDoctor(@PathVariable long doctorId) {
        try {
            int number = appointmentService.getAppointmentsToDoctor(doctorId).size();
            return new ResponseEntity<>(number, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getAppointmentsByDoctor(@PathVariable long doctorId) {
        try {
            return new ResponseEntity<>(appointmentService.getAppointmentsToDoctor(doctorId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
