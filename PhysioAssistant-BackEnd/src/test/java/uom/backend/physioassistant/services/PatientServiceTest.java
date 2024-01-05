package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import uom.backend.physioassistant.dtos.requests.CreatePatientRequest;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.PatientRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    private PatientService patientService;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        patientRepository = Mockito.mock(PatientRepository.class);
        patientService = new PatientService(patientRepository);

        System.out.println("=====================================");
        System.out.println("Starting test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("Finished test: " + testInfo.getDisplayName());
        System.out.println("=====================================");
    }

    @Test
    public void testCreatePatient_Success() {
        // Given
        CreatePatientRequest request = new CreatePatientRequest();
        request.setAmka("123456789");
        request.setName("John Doe");
        request.setAddress("123 Main St");

        Patient savedPatient = new Patient();
        savedPatient.setAmka("123456789");
        savedPatient.setName("John Doe");
        savedPatient.setAddress("123 Main St");
        savedPatient.setUsername("john.doe");
        savedPatient.setPassword("password");

        given(patientRepository.findById("123456789")).willReturn(Optional.empty());
        given(patientRepository.save(Mockito.any(Patient.class))).willReturn(savedPatient);

        // When
        Patient createdPatient = patientService.createPatient(request);

        // Then
        assertThat(savedPatient).isEqualTo(createdPatient);

        assertThat(createdPatient.getAmka()).isEqualTo("123456789");
        assertThat(createdPatient.getName()).isEqualTo("John Doe");
        assertThat(createdPatient.getAddress()).isEqualTo("123 Main St");
        assertThat(createdPatient.getUsername()).isEqualTo("john.doe");
        assertThat(createdPatient.getPassword()).isEqualTo("password");

        Mockito.verify(patientRepository, Mockito.times(1)).findById("123456789");
        Mockito.verify(patientRepository, Mockito.times(1)).save(Mockito.any(Patient.class));
    }

    @Test
    public void testCreatePatient_AlreadyAdded() {
        // Given
        CreatePatientRequest request = new CreatePatientRequest();
        request.setAmka("123456789");
        request.setName("John Doe");
        request.setAddress("123 Main St");

        Patient existingPatient = new Patient();
        existingPatient.setAmka("123456789");

        Mockito.when(patientRepository.findById("123456789")).thenReturn(Optional.of(existingPatient));

        // When and Then
        Assertions.assertThrows(AlreadyAddedException.class, () -> patientService.createPatient(request));

        Mockito.verify(patientRepository, Mockito.times(1)).findById("123456789");
        Mockito.verify(patientRepository, Mockito.never()).save(Mockito.any(Patient.class));
    }

    @Test
    public void testGetPatientById_Success() {
        // Given
        String patientId = "1";
        Patient expectedPatient = new Patient();
        expectedPatient.setAmka(patientId);

        given(patientRepository.findById(patientId)).willReturn(Optional.of(expectedPatient));

        // When
        Patient patient = patientService.getPatientById(patientId);

        // Then
        Assertions.assertEquals(expectedPatient, patient);

        Mockito.verify(patientRepository, Mockito.times(1)).findById(patientId);
    }

    @Test
    public void testGetPatientById_NotFound() {
        // Given
        String patientId = "1";

        given(patientRepository.findById(patientId)).willReturn(Optional.empty());

        // When and then
        Assertions.assertThrows(EntityNotFoundException.class, () -> patientService.getPatientById(patientId));

        Mockito.verify(patientRepository, Mockito.times(1)).findById(patientId);
    }

    @Test
    public void testGetAllPatients() {
        // Given
        List<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(new Patient());
        expectedPatients.add(new Patient());

        given(patientRepository.findAll()).willReturn(expectedPatients);

        // When
        Collection<Patient> patients = patientService.getAllPatients();

        // Then
        Assertions.assertEquals(expectedPatients, patients);

        Mockito.verify(patientRepository, Mockito.times(1)).findAll();
    }
}