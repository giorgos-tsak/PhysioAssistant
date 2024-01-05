package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uom.backend.physioassistant.models.Visit;

import java.util.Collection;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    @Query("SELECT visit FROM Visit visit WHERE visit.appointment.patient.username = ?1")
    Collection<Visit> findAllByPatientId(String patientId);
}
