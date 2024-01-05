package uom.android.physioassistant.models;

public enum UserType {
    ADMIN("ΠΣΦ"),
    DOCTOR("ΓΙΑΤΡΟΣ"),
    PATIENT("ΑΣΘΕΝΗΣ");

    private String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

