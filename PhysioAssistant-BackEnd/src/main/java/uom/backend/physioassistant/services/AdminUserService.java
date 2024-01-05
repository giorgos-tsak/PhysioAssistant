package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.exceptions.NotFoundException;
import uom.backend.physioassistant.models.users.Admin;
import uom.backend.physioassistant.repositories.AdminUserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;

    @Autowired
    public AdminUserService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    public Collection<Admin> getAllAdmins() {
        return this.adminUserRepository.findAll();
    }

    public Admin getAdminById(String id) {
        System.out.println("ID is "+id);
        Optional<Admin> foundAdmin = this.adminUserRepository.findById(Long.valueOf(id));

        if(foundAdmin.isEmpty())
            throw  new EntityNotFoundException("User with id: " + id + " not found.");

        return foundAdmin.get();
    }

    public Admin getAdminByUsername(String username) {
        Optional<Admin> foundAdmin = this.adminUserRepository.findByUsername(username);

        if (foundAdmin.isEmpty()) {
            throw new EntityNotFoundException("User with username: " + username + " not found.");
        }

        return foundAdmin.get();
    }

    public boolean checkIfUserExists(String username) {
        Optional<Admin> foundAdmin = this.adminUserRepository.findByUsername(username);

        if (foundAdmin.isEmpty())
            return false;
        return true;
    }


    public Admin addAdminUser(Admin user) {
        String givenUsername = user.getUsername();
        Optional<Admin> foundAdmin = this.adminUserRepository.findByUsername(givenUsername);

        if (foundAdmin.isPresent())
                throw new AlreadyAddedException("Το username: " + givenUsername + " υπάρχει ήδη");
        return (Admin) adminUserRepository.save(user);
    }

}
