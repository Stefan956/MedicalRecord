package com.cscb869.MedicalRecord.controller;

import com.cscb869.MedicalRecord.commons.paths.Constants;
import com.cscb869.MedicalRecord.dto.SpecializationDTO;
import com.cscb869.MedicalRecord.exception.NotFoundException;
import com.cscb869.MedicalRecord.model.Specialization;
import com.cscb869.MedicalRecord.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = Constants.API_PATH + Constants.SPECIALIZATION_PATH)
public class SpecializationController {
    @Autowired
    private SpecializationService specializationService;

    public SpecializationController() {

    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllSpecializations() {
        List<SpecializationDTO> specializations = specializationService.getAllSpecializations();

        return new ResponseEntity<>(specializations, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getSpecializationById(@PathVariable long id) {
        try {
            SpecializationDTO specializationResponse = specializationService.getSpecializationById(id);

            return new ResponseEntity<>(specializationResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getSpecializationByName(@PathVariable String name) {
        try {
            SpecializationDTO specializationResponse = specializationService.getSpecializationByName(name);

            return new ResponseEntity<>(specializationResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSpecialization(@Valid @RequestBody Specialization specialization) {
        try {
            SpecializationDTO specializationResponse = specializationService.createSpecialization(specialization);

            return new ResponseEntity<>(specializationResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSpecialization(@Valid @RequestBody Specialization specialization, @PathVariable long id) {
        try {
            specialization.setId(id);
            SpecializationDTO specializationResponse = specializationService.createSpecialization(specialization);

            return new ResponseEntity<>(specializationResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSpecialization(@PathVariable long id) {
        try {
            specializationService.deleteSpecialization(id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
