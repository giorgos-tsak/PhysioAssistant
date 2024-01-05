package uom.backend.physioassistant.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.dtos.requests.CreateAppointmentRequest;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.services.AppointmentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping()
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = (List) this.appointmentService.getAllAppointments();

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = this.appointmentService.getAppointmentById(id);

            return ResponseEntity.ok()
                    .body(appointment);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@RequestParam(name = "did") String doctorId) {
        List<Appointment> appointments = (List) this.appointmentService.getAppointmentsBasedOnDoctorId(doctorId);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/patient")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(@RequestParam(name = "pid") String patientId) {
        List<Appointment> appointments = (List) this.appointmentService.getAppointmentsBasedOnPatientId(patientId);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/{patientId}/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForPatientWithDoctor( @PathVariable String patientId,@PathVariable String doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForPatientWithDoctor(patientId, doctorId);
        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/doctor/{doctorId}/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsForDoctorByStatus(
            @PathVariable String doctorId, @PathVariable AppointmentStatus status
    ){
        List<Appointment> appointments = (List) this.appointmentService.getAllForDoctorByStatus(doctorId, status);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @GetMapping("/patient/{patientId}/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsForPatientByStatus(
            @PathVariable String patientId, @PathVariable AppointmentStatus status
    ){
        List<Appointment> appointments = (List) this.appointmentService.getAllForPatientByStatus(patientId, status);

        return ResponseEntity.ok()
                .body(appointments);
    }

    @PutMapping("/update/status")
    public ResponseEntity updateAppointmentStatus(
            @RequestParam(name = "aid") Long appointmentId,
            @RequestParam(name = "status") AppointmentStatus status) {

        try {
            this.appointmentService.setAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok()
                    .build();
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody CreateAppointmentRequest appointmentRequest) {
        Appointment createdAppointment = this.appointmentService.createAppointment(appointmentRequest);

        return ResponseEntity.ok()
                .body(createdAppointment);
    }
}
