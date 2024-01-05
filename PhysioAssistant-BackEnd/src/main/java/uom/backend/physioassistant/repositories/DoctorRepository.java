package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
    @Query("SELECT d FROM Doctor d WHERE d.username = ?1")
    Optional<Doctor> findByUsername(String username);

}
