package com.cscb869.MedicalRecord.controller;

import com.cscb869.MedicalRecord.dto.AppointmentDTO;
import com.cscb869.MedicalRecord.dto.DoctorDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Appointment;
import com.cscb869.MedicalRecord.model.Doctor;
import com.cscb869.MedicalRecord.service.AppointmentService;
import com.cscb869.MedicalRecord.commons.paths.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = Constants.API_PATH + Constants.APPOINTMENT_PATH)
public class AppointmentController {
    private AppointmentService appointmentService;

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
            AppointmentDTO appointmentResponse = appointmentService.getAppointmentDtoById(id);

            return new ResponseEntity<>(appointmentResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment appointment) {
        try {
            AppointmentDTO appointmentResponse = appointmentService.createAppointment(appointment);

            return new ResponseEntity<>(appointmentResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(@Valid @RequestBody Appointment appointment, @PathVariable long id) {
        try {
            appointment.setId(id);
            AppointmentDTO appointmentResponse = appointmentService.updateAppointment(appointment);

            return new ResponseEntity<>(appointmentResponse, HttpStatus.OK);
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
}
