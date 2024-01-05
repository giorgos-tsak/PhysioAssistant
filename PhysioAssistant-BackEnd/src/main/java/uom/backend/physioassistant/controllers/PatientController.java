package uom.backend.physioassistant.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.auth.Authentication;
import uom.backend.physioassistant.dtos.requests.CreatePatientRequest;
import uom.backend.physioassistant.dtos.requests.LoginRequest;
import uom.backend.physioassistant.dtos.responses.LoginResponse;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.services.PatientService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController implements Authentication {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    public ResponseEntity<List> getAllPatients() {
        List<Patient> patients = (List) patientService.getAllPatients();

        return ResponseEntity.ok()
                .body(patients);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        try {
            Patient foundPatient = patientService.getPatientById(id);

            return ResponseEntity.ok()
                    .body(foundPatient);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Patient> getPatientByUsername(@PathVariable String username) {
        try {
            Patient foundPatient = patientService.getPatientByUsername(username);

            return ResponseEntity.ok()
                    .body(foundPatient);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @GetMapping("/doctors/{patientId}")
    public ResponseEntity<List<Doctor>> getAllDoctorsByPatientId(@PathVariable String patientId) {
        try {
            List<Doctor> doctors = patientService.getAllDoctorsByPatientId(patientId);

            return ResponseEntity.ok()
                    .body(doctors);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePatient(@PathVariable String id) {
        try {
            patientService.deletePatientById(id);
            return ResponseEntity.ok()
                    .build();
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            Patient foundPatient = patientService.getPatientByUsername(username);
            String correctPassword = foundPatient.getPassword();

            // Check if password is correct
            if (password.equals(correctPassword))
                return ResponseEntity.ok(new LoginResponse(foundPatient));
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("Wrong username or password."));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Wrong username or password."));
        }
    }
}
