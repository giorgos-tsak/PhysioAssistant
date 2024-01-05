package uom.backend.physioassistant.services;

import org.springframework.stereotype.Service;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.exceptions.NotFoundException;
import uom.backend.physioassistant.models.PhysioAction;
import uom.backend.physioassistant.models.users.Admin;
import uom.backend.physioassistant.repositories.PhysioActionRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class PhysioActionService {
    private final PhysioActionRepository physioActionRepository;

    public PhysioActionService(PhysioActionRepository physioActionRepository) {
        this.physioActionRepository = physioActionRepository;
    }

    public Collection<PhysioAction> getAllActions() {
        return this.physioActionRepository.findAll();
    }

    public PhysioAction getById(String id) {
        Optional<PhysioAction> foundAction = physioActionRepository.findById(id);

        if (foundAction.isEmpty())
            throw new NotFoundException("Physiotherapy Action with id: " + id + " not found.");

        return foundAction.get();
    }

    public void deleteById(String id) {
        PhysioAction foundAction = this.getById(id);
        this.physioActionRepository.delete(foundAction);
    }

    public PhysioAction createPhysioAction(PhysioAction physioAction) {
        String givenId = physioAction.getCode();
        Optional<PhysioAction> foundAction = this.physioActionRepository.findById(givenId);

        if (foundAction.isPresent())
            throw new AlreadyAddedException("Action with id: " + givenId + " is already added.");
        return physioActionRepository.save(physioAction);
    }
}
