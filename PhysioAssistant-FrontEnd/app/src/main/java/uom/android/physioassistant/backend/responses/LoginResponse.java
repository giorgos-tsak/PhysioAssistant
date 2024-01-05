package uom.android.physioassistant.backend.responses;

import uom.android.physioassistant.models.User;
public class LoginResponse {
    private User user;
    private String errorMessage;
    public LoginResponse(User user) {
        this.user = user;
    }

    public LoginResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LoginResponse() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "user=" + user +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

