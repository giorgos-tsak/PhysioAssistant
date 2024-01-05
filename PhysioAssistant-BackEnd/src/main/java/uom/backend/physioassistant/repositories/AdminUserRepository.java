package uom.backend.physioassistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uom.backend.physioassistant.models.users.Admin;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT a FROM Admin a WHERE a.username = ?1")
    Optional<Admin> findByUsername(String username);
}
