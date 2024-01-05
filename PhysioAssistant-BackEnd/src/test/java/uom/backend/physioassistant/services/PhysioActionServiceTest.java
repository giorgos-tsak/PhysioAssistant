package uom.backend.physioassistant.services;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.exceptions.NotFoundException;
import uom.backend.physioassistant.models.PhysioAction;
import uom.backend.physioassistant.repositories.PhysioActionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class PhysioActionServiceTest {
    private PhysioActionRepository physioActionRepository;
    private PhysioActionService physioActionService;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        physioActionRepository = Mockito.mock(PhysioActionRepository.class);
        physioActionService = new PhysioActionService(physioActionRepository);

        System.out.println("=====================================");
        System.out.println("Starting test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("Finished test: " + testInfo.getDisplayName());
        System.out.println("=====================================");
    }

    @Test
    public void testCreatePhysioAction_ShouldCreatePhysioAction() {
        // Given
        PhysioAction physioAction = new PhysioAction();
        physioAction.setCode("1");
        physioAction.setName("Massage");

        given(physioActionRepository.findById("1")).willReturn(Optional.empty());
        given(physioActionRepository.save(Mockito.any(PhysioAction.class))).willReturn(physioAction);

        // When
        PhysioAction createdPhysioAction = physioActionService.createPhysioAction(physioAction);

        // Then
        assertEquals(physioAction, createdPhysioAction);

        Mockito.verify(physioActionRepository, Mockito.times(1)).findById("1");
        Mockito.verify(physioActionRepository, Mockito.times(1)).save(Mockito.any(PhysioAction.class));
    }

    @Test
    public void testCreatePhysioAction_ShouldThrowAlreadyAddedException() {
        // Given
        PhysioAction physioAction = new PhysioAction();
        physioAction.setCode("1");
        physioAction.setName("Massage");

        given(physioActionRepository.findById("1")).willReturn(Optional.of(physioAction));

        // When, Then
        assertThrows(AlreadyAddedException.class, () -> physioActionService.createPhysioAction(physioAction));

        Mockito.verify(physioActionRepository, Mockito.times(1)).findById("1");
        Mockito.verify(physioActionRepository, Mockito.never()).save(Mockito.any(PhysioAction.class));
    }


    @Test
    public void testGetById_ShouldRetrievePhysioAction() {
        // Given
        String physioActionId = "1";
        PhysioAction expectedPhysioAction = new PhysioAction();
        expectedPhysioAction.setCode(physioActionId);

        Mockito.when(physioActionRepository.findById(physioActionId)).thenReturn(Optional.of(expectedPhysioAction));

        // When
        PhysioAction physioAction = physioActionService.getById(physioActionId);

        // Then
        assertThat(physioAction).isEqualTo(expectedPhysioAction);

        Mockito.verify(physioActionRepository, Mockito.times(1)).findById(physioActionId);
    }

    @Test
    public void testGetById_ShouldThrowNotFoundException() {
        // Given
        String physioActionId = "1";

        given(physioActionRepository.findById(physioActionId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(NotFoundException.class, () -> physioActionService.getById(physioActionId));

        Mockito.verify(physioActionRepository, Mockito.times(1)).findById(physioActionId);
    }


    @Test
    public void testGetAllActions_ShouldReturnAllActions() {
        // Given
        List<PhysioAction> expectedActions = new ArrayList<>();
        expectedActions.add(new PhysioAction());
        expectedActions.add(new PhysioAction());

        given(physioActionRepository.findAll()).willReturn(expectedActions);

        // When
        Collection<PhysioAction> actions = physioActionService.getAllActions();

        // Then
        assertThat(actions).isEqualTo(expectedActions);

        Mockito.verify(physioActionRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testDeleteById_ShouldDeletePhysioAction() {
        // Given
        String physioActionId = "1";
        PhysioAction physioAction = new PhysioAction();
        physioAction.setCode(physioActionId);

        given(physioActionRepository.findById(physioActionId)).willReturn(Optional.of(physioAction));

        // When
        physioActionService.deleteById(physioActionId);

        // Then
        Mockito.verify(physioActionRepository, Mockito.times(1)).findById(physioActionId);
        Mockito.verify(physioActionRepository, Mockito.times(1)).delete(physioAction);
    }

    @Test
    public void testDeleteById_ShouldThrowNotFoundException() {
        // Given
        String physioActionId = "1";

        given(physioActionRepository.findById(physioActionId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(NotFoundException.class, () -> physioActionService.deleteById(physioActionId));

        Mockito.verify(physioActionRepository, Mockito.times(1)).findById(physioActionId);
        Mockito.verify(physioActionRepository, Mockito.never()).delete(Mockito.any(PhysioAction.class));
    }
}