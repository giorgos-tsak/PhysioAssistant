package uom.backend.physioassistant.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import uom.backend.physioassistant.models.users.User;

@Getter @Setter
public class LoginResponse {
    private User user;
    private String errorMessage;
    public LoginResponse(User user) {
        this.user = user;
    }

    public LoginResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

