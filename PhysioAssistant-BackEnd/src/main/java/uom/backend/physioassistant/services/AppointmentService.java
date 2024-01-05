package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uom.backend.physioassistant.dtos.requests.CreateAppointmentRequest;
import uom.backend.physioassistant.models.PhysioAction;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PhysioActionService physioActionService;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorService doctorService, PatientService patientService, PhysioActionService physioActionService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.physioActionService = physioActionService;
    }

    public Appointment getAppointmentById(Long id) {
        return this.appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with id: " + id + " not found."));
    }

    public Appointment createAppointment(CreateAppointmentRequest appointmentRequest) {
        Appointment appointmentToSave = new Appointment();

        String givenDoctorId = appointmentRequest.getDoctorId();
        String givenPatientId = appointmentRequest.getPatientId();
        String givePhysioActionId = appointmentRequest.getPhysioActionId();
        LocalDate date = appointmentRequest.getDate();
        LocalTime time = appointmentRequest.getTime();

        Doctor doctor = this.doctorService.getById(givenDoctorId);
        Patient patient = this.patientService.getPatientById(givenPatientId);
        PhysioAction physioAction = this.physioActionService.getById(givePhysioActionId);

        appointmentToSave.setDoctor(doctor);
        appointmentToSave.setPatient(patient);
        appointmentToSave.setPhysioAction(physioAction);
        appointmentToSave.setDate(date);
        appointmentToSave.setTime(time);

        return this.appointmentRepository.save(appointmentToSave);
    }

    public Collection<Appointment> getAllAppointments() {
        return this.appointmentRepository.findAll();
    }

    public Collection<Appointment> getAppointmentsBasedOnDoctorId(String doctorId) {
        return this.appointmentRepository.findAllByDoctorId(doctorId);
    }

    public Collection<Appointment> getAppointmentsBasedOnPatientId(String patientId) {
        return this.appointmentRepository.findAllByPatientId(patientId);
    }

    public Collection<Appointment> getAllForDoctorByStatus(String doctorId, AppointmentStatus status) {
        return this.appointmentRepository.findAllForDoctorByStatus(doctorId, status);
    }

    public Collection<Appointment> getAllForPatientByStatus(String patientId, AppointmentStatus status) {
        return this.appointmentRepository.findAllForPatientByStatus(patientId, status);
    }

    public void setAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = this.getAppointmentById(appointmentId);
        appointment.setStatus(status);
        this.appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsForPatientWithDoctor(String patientId, String doctorId) {
        Patient patient = patientService.getPatientById(patientId);
        Doctor doctor = doctorService.getById(doctorId);

        if(patient==null)
            throw new EntityNotFoundException("Patient with id "+patientId+" not found");

        if(doctor==null)
            throw new EntityNotFoundException("Doctor with id "+doctorId+" not found");


        return appointmentRepository.getAppointmentsForPatientWithDoctor(patientId, doctorId);
    }
}
