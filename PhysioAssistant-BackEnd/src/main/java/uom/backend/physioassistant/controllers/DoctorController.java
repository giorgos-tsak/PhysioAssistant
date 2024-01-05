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
import uom.backend.physioassistant.services.DoctorService;
import uom.backend.physioassistant.services.PatientService;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController implements Authentication {
    private final DoctorService doctorService;
    private final PatientService patientService;

    public DoctorController(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping()
    public ResponseEntity<List> getAllDoctors() {
        List<Doctor> doctors = (List) doctorService.getAllDoctors();

        return ResponseEntity.ok()
                .body(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable String id) {
        try{
            Doctor doctor = this.doctorService.getById(id);

            return ResponseEntity.ok()
                    .body(doctor);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Doctor> getDoctorByUsername(@PathVariable String username) {
        try {
            Doctor foundDoctor = doctorService.getDoctorByUsername(username);

            return ResponseEntity.ok()
                    .body(foundDoctor);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<List<Patient>> getAllPatientsByDoctorId(@PathVariable String doctorId){
        List<Patient> patients = doctorService.getAllPatientsByDoctorId(doctorId);
        return ResponseEntity.ok().body(patients);
    }



    // Will be used for R1
    @PostMapping("/create")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        // Make sure the doctor is not already added
        try {
            this.doctorService.createDoctor(doctor);

            return ResponseEntity.ok()
                    .body(doctor);
        }
        catch (AlreadyAddedException e) {
            String errorMsg = e.getMessage();
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(errorMsg);
        }
        catch (Exception e) {
            String errorMsg = "Παρουσιάστηκε σφάλμα κατά τη δημιουργία του γιατρού.";
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(errorMsg);
        }
    }

    @PostMapping("/create-patient")
    public ResponseEntity<Patient> createPatient(@RequestParam String doctorId,  @RequestBody CreatePatientRequest patientRequest){

        Patient patient = doctorService.createPatient(doctorId,patientRequest);

        return ResponseEntity.ok().body(patient);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable String id) {
        doctorService.deleteById(id);

        return ResponseEntity.ok()
                .build();
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            Doctor foundDoctor = doctorService.getDoctorByUsername(username);
            String correctPassword = foundDoctor.getPassword();

            // Validate Password
            if (password.equals(correctPassword))
                return ResponseEntity.ok(new LoginResponse(foundDoctor));
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("Λάθος username ή/και password."));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("To username: " + loginRequest.getUsername() + " δεν αντιστοιχεί σε κάποιον χρήστη."));
        }
    }
}
