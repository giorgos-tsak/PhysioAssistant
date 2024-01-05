package uom.backend.physioassistant.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uom.backend.physioassistant.auth.Authentication;
import uom.backend.physioassistant.models.users.Admin;
import uom.backend.physioassistant.dtos.requests.LoginRequest;
import uom.backend.physioassistant.dtos.responses.LoginResponse;
import uom.backend.physioassistant.services.AdminUserService;
import uom.backend.physioassistant.services.PhysioActionService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController implements Authentication {
    private final AdminUserService adminUserService;
    private final PhysioActionService physioActionService;

    public AdminController(AdminUserService adminUserService, PhysioActionService physioActionService) {
        this.adminUserService = adminUserService;
        this.physioActionService = physioActionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List> getAllAdmins() {
        List<Admin> admins = (List) adminUserService.getAllAdmins();

        return ResponseEntity.ok()
                .body(admins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String id) {
        try {
            Admin found_admin =  adminUserService.getAdminById(id);

            return ResponseEntity.ok()
                    .body(found_admin);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin adminToAdd) {
        this.adminUserService.addAdminUser(adminToAdd);

        return ResponseEntity.ok()
                .body(adminToAdd);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        boolean userExists = this.adminUserService.checkIfUserExists(username);

        if (!userExists)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Wrong username or password."));

        Admin admin = this.adminUserService.getAdminByUsername(username);

        String correctPassword = admin.getPassword();

        boolean passwordIsCorrect = password.equals(correctPassword);

        if (passwordIsCorrect)
            return ResponseEntity.ok(new LoginResponse(admin));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Wrong username or password."));
    }
}
