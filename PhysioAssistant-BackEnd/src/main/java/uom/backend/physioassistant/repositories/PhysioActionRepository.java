package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uom.backend.physioassistant.models.PhysioAction;

public interface PhysioActionRepository extends JpaRepository<PhysioAction, String> {
}
