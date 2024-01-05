package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uom.backend.physioassistant.dtos.requests.CreatePatientRequest;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.DoctorRepository;
import uom.backend.physioassistant.repositories.PatientRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class DoctorServiceTest {
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;


    @BeforeEach
    void setUp(TestInfo testInfo) {
        MockitoAnnotations.openMocks(this);
        doctorService = new DoctorService(doctorRepository, patientService, patientRepository);

        System.out.println("=====================================");
        System.out.println("Starting test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("Finished test: " + testInfo.getDisplayName());
        System.out.println("=====================================");
    }

    @Test
    void testGetAllDoctors_ShouldReturnAllDoctors() {
        // Given
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor());
        doctors.add(new Doctor());

        given(doctorRepository.findAll()).willReturn(doctors);

        // When
        Collection<Doctor> result = doctorService.getAllDoctors();

        // Then
        assertEquals(doctors.size(), result.size());
        assertTrue(result.containsAll(doctors));
    }

    @Test
    void testGetById_ValidId_ShouldReturnDoctor() {
        // Given
        String id = "1";
        Doctor doctor = new Doctor();
        given(doctorRepository.findById(id)).willReturn(Optional.of(doctor));

        // When
        Doctor result = doctorService.getById(id);

        // Then
        assertEquals(doctor, result);
    }


    @Test
    void testGetById_NonExistingId_ShouldThrowEntityNotFoundException() {
        // Given
        String id = "1";
        given(doctorRepository.findById(id)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> doctorService.getById(id));
        verify(doctorRepository, times(1)).findById(id);
    }

    @Test
    void testGetDoctorByUsername_ExistingUsername_ShouldReturnDoctor() {
        // Given
        String username = "john_doe";
        Doctor doctor = new Doctor();
        given(doctorRepository.findByUsername(username)).willReturn(Optional.of(doctor));

        // When
        Doctor result = doctorService.getDoctorByUsername(username);

        // Then
        assertEquals(doctor, result);
        verify(doctorRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetDoctorByUsername_NonExistingUsername_ShouldThrowEntityNotFoundException() {
        // Given
        String username = "john_doe";
        given(doctorRepository.findByUsername(username)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> doctorService.getDoctorByUsername(username));
        verify(doctorRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetAllPatientsByDoctorId_ExistingDoctorId_ShouldReturnAllPatients() {
        // Given
        String doctorId = "1";
        Doctor doctor = new Doctor();
        doctor.setAfm(doctorId);

        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient());
        patients.add(new Patient());

        doctor.setPatients(patients);

        given(doctorRepository.findById(doctorId)).willReturn(Optional.of(doctor));

        // When
        List<Patient> result = doctorService.getAllPatientsByDoctorId(doctorId);

        // Then
        assertThat(result).hasSize(2);
        verify(doctorRepository, times(1)).findById(doctorId);
    }

    @Test
    void testGetAllPatientsByDoctorId_NonExistingDoctorId_ShouldThrowEntityNotFoundException() {
        // Given
        String doctorId = "1";
        given(doctorRepository.findById(doctorId)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> doctorService.getAllPatientsByDoctorId(doctorId));
        verify(doctorRepository, times(1)).findById(doctorId);
    }

    @Test
    void testGetOptionalById_ExistingDoctorId_ShouldReturnOptionalDoctor() {
        // Given
        String id = "1";
        Doctor doctor = new Doctor();
        given(doctorRepository.findById(id)).willReturn(Optional.of(doctor));

        // When
        Optional<Doctor> result = doctorService.getOptionalById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(doctor, result.get());
        verify(doctorRepository, times(1)).findById(id);
    }

    @Test
    void testGetOptionalById_NonExistingDoctorId_ShouldReturnEmptyOptional() {
        // Given
        String id = "1";
        given(doctorRepository.findById(id)).willReturn(Optional.empty());

        // When
        Optional<Doctor> result = doctorService.getOptionalById(id);

        // Then
        assertFalse(result.isPresent());
        verify(doctorRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById_ExistingDoctorId_ShouldDeleteDoctor() {
        // Given
        String id = "1";
        Doctor doctor = new Doctor();
        given(doctorRepository.findById(id)).willReturn(Optional.of(doctor));

        // When
        doctorService.deleteById(id);

        // Then
        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void testDeleteById_NonExistingDoctorId_ShouldThrowEntityNotFoundException() {
        // Given
        String id = "1";
        given(doctorRepository.findById(id)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> doctorService.deleteById(id));
        verify(doctorRepository, never()).delete(any());
    }

    @Test
    void testCreateDoctor_NewDoctor_ShouldReturnCreatedDoctor() {
        // Given
        Doctor doctor = new Doctor();
        doctor.setUsername("john_doe");

        given(doctorRepository.findById(doctor.getUsername())).willReturn(Optional.empty());
        given(doctorRepository.save(doctor)).willReturn(doctor);

        // When
        Doctor result = doctorService.createDoctor(doctor);

        // Then
        assertEquals(doctor, result);
    }

    @Test
    void testCreateDoctor_DuplicateDoctor_ShouldThrowAlreadyAddedException() {
        // Given
        Doctor doctor = new Doctor();
        doctor.setUsername("john_doe");
        given(doctorRepository.findByUsername(doctor.getUsername())).willReturn(Optional.of(doctor));

        // When, Then
        assertThrows(AlreadyAddedException.class, () -> doctorService.createDoctor(doctor));
    }

    @Test
    void testCreatePatient_NonExistingDoctor_ShouldThrowEntityNotFoundException() {
        // Given
        String doctorId = "1";
        CreatePatientRequest patientRequest = new CreatePatientRequest();
        patientRequest.setAmka("1234567890");

        given(doctorRepository.findById(doctorId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> doctorService.createPatient(doctorId, patientRequest));
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(patientRepository, never()).findById(any());
        verify(patientService, never()).createPatient(any());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void testCreatePatient_ExistingDoctorAndPatient_ShouldThrowAlreadyAddedException() {
        // Given
        String doctorId = "1";

        CreatePatientRequest patientRequest = new CreatePatientRequest();
        patientRequest.setAmka("1234567890");

        Doctor doctor = new Doctor();
        doctor.setAfm(doctorId);

        Patient existingPatient = new Patient();
        existingPatient.setAmka(patientRequest.getAmka());
        existingPatient.setDoctors(Arrays.asList(doctor));

        doctor.getPatients().add(existingPatient);

        given(doctorRepository.findById(doctorId)).willReturn(Optional.of(doctor));
        given(patientRepository.findById(patientRequest.getAmka())).willReturn(Optional.of(existingPatient));

        // When and Then
        assertThrows(AlreadyAddedException.class, () -> doctorService.createPatient(doctorId, patientRequest));
        verify(patientRepository, never()).save(any());
    }
}