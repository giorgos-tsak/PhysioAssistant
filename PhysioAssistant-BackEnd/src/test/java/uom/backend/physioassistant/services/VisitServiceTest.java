package uom.backend.physioassistant.services;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import uom.backend.physioassistant.dtos.requests.CreateVisitRequest;
import uom.backend.physioassistant.models.PhysioAction;
import uom.backend.physioassistant.models.Visit;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.repositories.VisitRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class VisitServiceTest {
    @Mock
    private VisitRepository visitRepository;
    @Mock
    private AppointmentService appointmentService;
    private VisitService visitService;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        visitRepository = Mockito.mock(VisitRepository.class);
        appointmentService = Mockito.mock(AppointmentService.class);

        visitService = new VisitService(visitRepository, appointmentService);
        System.out.println("=====================================");
        System.out.println("Starting test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("Finished test: " + testInfo.getDisplayName());
        System.out.println("=====================================");
    }

    @Test
    public void testCreateVisit_ShouldCreateVisit() {
        // Given
        long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        List<PhysioAction> services = new ArrayList<>();
        services.add(new PhysioAction());

        CreateVisitRequest visitRequest = new CreateVisitRequest();
        visitRequest.setAppointmentId(appointmentId);
        visitRequest.setServices(services);

        given(appointmentService.getAppointmentById(appointmentId)).willReturn(appointment);
        Mockito.when(visitRepository.save(Mockito.any(Visit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Visit createdVisit = visitService.createVisit(visitRequest);

        // Then
        Assertions.assertNotNull(createdVisit);
        Assertions.assertEquals(appointment, createdVisit.getAppointment());
        Assertions.assertEquals(services, createdVisit.getPhysioActions());

        Mockito.verify(appointmentService, Mockito.times(1)).getAppointmentById(appointmentId);
        Mockito.verify(visitRepository, Mockito.times(1)).save(Mockito.any(Visit.class));
    }

    @Test
    public void testGetAllVisits_ShouldReturnAllVisits() {
        // Given
        List<Visit> expectedVisits = new ArrayList<>();
        expectedVisits.add(new Visit());
        expectedVisits.add(new Visit());

        given(visitRepository.findAll()).willReturn(expectedVisits);
        // When
        Collection<Visit> visits = visitService.getAllVisits();

        // Then
        assertThat(visits).isEqualTo(expectedVisits);

        Mockito.verify(visitRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetVisitsByPatientId_ShouldReturnVisits() {
        // Given
        String patientId = "1";
        List<Visit> expectedVisits = new ArrayList<>();
        expectedVisits.add(new Visit());
        expectedVisits.add(new Visit());

        given(visitRepository.findAllByPatientId(patientId)).willReturn(expectedVisits);

        // When
        Collection<Visit> visits = visitService.getVisitsByPatientId(patientId);

        // Then
        assertThat(visits).isEqualTo(expectedVisits);

        Mockito.verify(visitRepository, Mockito.times(1)).findAllByPatientId(patientId);
    }}