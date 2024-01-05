package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uom.backend.physioassistant.dtos.requests.CreateAppointmentRequest;
import uom.backend.physioassistant.models.PhysioAction;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.models.appointment.AppointmentStatus;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @Mock
    private PhysioActionService physioActionService;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        MockitoAnnotations.openMocks(this);

        System.out.println("=====================================");
        System.out.println("Starting test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("Finished test: " + testInfo.getDisplayName());
        System.out.println("=====================================");
    }


    @Test
    void testGetAppointmentById_ShouldReturnAppointment() {
        // Given
        long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        given(appointmentRepository.findById(appointmentId))
                .willReturn(Optional.of(appointment));

        // When
        Appointment result = appointmentService.getAppointmentById(appointmentId);

        // Then
        assertEquals(appointment, result);
    }

    @Test
    void testGetAppointmentById_NonExistingId_ShouldThrowEntityNotFoundException() {
        // Given
        long appointmentId = 1L;

        given(appointmentRepository.findById(appointmentId))
                .willReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> appointmentService.getAppointmentById(appointmentId));
    }

    @Test
    void testCreateAppointment_ValidRequest_ShouldReturnCreatedAppointment() {
        // Given
        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setDoctorId("doctorId");
        request.setPatientId("patientId");
        request.setPhysioActionId("physioActionId");
        request.setDate(LocalDate.now());
        request.setTime(LocalTime.now());

        Doctor doctor = new Doctor();
        given(doctorService.getById(request.getDoctorId()))
                .willReturn(doctor);

        Patient patient = new Patient();
        given(patientService.getPatientById(request.getPatientId()))
                .willReturn(patient);

        PhysioAction physioAction = new PhysioAction();
        given(physioActionService.getById(request.getPhysioActionId()))
                .willReturn(physioAction);

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setPatient(patient);
        savedAppointment.setDoctor(doctor);
        savedAppointment.setPhysioAction(physioAction);
        savedAppointment.setDate(request.getDate());
        savedAppointment.setTime(request.getTime());

        given(appointmentRepository.save(any(Appointment.class)))
                .willReturn(savedAppointment);

        // When
        Appointment result = appointmentService.createAppointment(request);

        // Then
        assertEquals(savedAppointment, result);
        assertEquals(doctor, result.getDoctor());
        assertEquals(patient, result.getPatient());
        assertEquals(physioAction, result.getPhysioAction());
        assertEquals(request.getDate(), result.getDate());
        assertEquals(request.getTime(), result.getTime());

        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testGetAllAppointments_ShouldReturnAllAppointments() {
        // Given
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();

        given(appointmentRepository.findAll())
                .willReturn(Arrays.asList(appointment1, appointment2));

        // When
        Collection<Appointment> result = appointmentService.getAllAppointments();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(appointment1));
        assertTrue(result.contains(appointment2));
    }


    @Test
    void testGetAppointmentsBasedOnDoctorId_ShouldReturnAppointmentsForDoctor() {
        // Given
        String doctorId = "doctorId";
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();

        given(appointmentRepository.findAllByDoctorId(doctorId))
                .willReturn(Arrays.asList(appointment1, appointment2));

        // When
        Collection<Appointment> result = appointmentService.getAppointmentsBasedOnDoctorId(doctorId);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(appointment1));
        assertTrue(result.contains(appointment2));
    }

    @Test
    void testGetAppointmentsBasedOnPatientId_ShouldReturnAppointmentsForPatient() {
        // Given
        String patientId = "patientId";
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();

        given(appointmentRepository.findAllByPatientId(patientId))
                .willReturn(Arrays.asList(appointment1, appointment2));
        // When
        Collection<Appointment> result = appointmentService.getAppointmentsBasedOnPatientId(patientId);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(appointment1));
        assertTrue(result.contains(appointment2));
    }

    @Test
    void testGetAllForDoctorByStatus_ShouldReturnAppointmentsForDoctorWithStatus() {
        // Given
        String doctorId = "doctorId";
        AppointmentStatus status = AppointmentStatus.ACCEPTED;
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();

        given(appointmentRepository.findAllForDoctorByStatus(doctorId, status))
                .willReturn(Arrays.asList(appointment1, appointment2));

        // When
        Collection<Appointment> result = appointmentService.getAllForDoctorByStatus(doctorId, status);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(appointment1));
        assertTrue(result.contains(appointment2));
    }

    @Test
    void testGetAllForPatientByStatus_ShouldReturnAppointmentsForPatientWithStatus() {
        // Given
        String patientId = "patientId";
        AppointmentStatus status = AppointmentStatus.ACCEPTED;
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();

        given(appointmentRepository.findAllForPatientByStatus(patientId, status))
                .willReturn(Arrays.asList(appointment1, appointment2));

        // When
        Collection<Appointment> result = appointmentService.getAllForPatientByStatus(patientId, status);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(appointment1));
        assertTrue(result.contains(appointment2));
    }

    @Test
    void testSetAppointmentStatus_ValidAppointmentId_ShouldUpdateAppointmentStatus() {
        // Given
        long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.PENDING);

        given(appointmentRepository.findById(appointmentId))
                .willReturn(Optional.of(appointment));

        // When
        appointmentService.setAppointmentStatus(appointmentId, AppointmentStatus.ACCEPTED);

        // Then
        assertEquals(AppointmentStatus.ACCEPTED, appointment.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void testSetAppointmentStatus_NonExistingAppointmentId_ShouldThrowEntityNotFoundException() {
        // Given
        long appointmentId = 1L;

        given(appointmentRepository.findById(appointmentId))
                .willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.setAppointmentStatus(appointmentId, AppointmentStatus.ACCEPTED));
    }

    @Test
    void testGetAppointmentsForPatientWithDoctor_ValidIds_ShouldReturnAppointments() {
        // Given
        String patientId = "patientId";
        String doctorId = "doctorId";
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        List<Appointment> appointments = Arrays.asList(new Appointment(), new Appointment());

        given(patientService.getPatientById(patientId)).willReturn(patient);
        given(doctorService.getById(doctorId)).willReturn(doctor);
        given(appointmentRepository.getAppointmentsForPatientWithDoctor(patientId, doctorId)).willReturn(appointments);

        // When
        List<Appointment> result = appointmentService.getAppointmentsForPatientWithDoctor(patientId, doctorId);

        // Then
        assertEquals(appointments, result);
    }

    @Test
    void testGetAppointmentsForPatientWithDoctor_InvalidPatientId_ShouldThrowEntityNotFoundException() {
        // Given
        String patientId = "patientId";
        String doctorId = "doctorId";
        given(patientService.getPatientById(patientId)).willReturn(null);

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.getAppointmentsForPatientWithDoctor(patientId, doctorId));

        verify(appointmentRepository, never()).getAppointmentsForPatientWithDoctor(anyString(), anyString());
    }

    @Test
    void testGetAppointmentsForPatientWithDoctor_InvalidDoctorId_ShouldThrowEntityNotFoundException() {
        // Given
        String patientId = "patientId";
        String doctorId = "doctorId";

        Patient patient = new Patient();
        patient.setAmka(patientId);

        given(patientService.getPatientById(patientId)).willReturn(patient);
        given(doctorService.getById(doctorId)).willReturn(null);

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.getAppointmentsForPatientWithDoctor(patientId, doctorId));

        verify(appointmentRepository, never()).getAppointmentsForPatientWithDoctor(anyString(), anyString());
    }
}