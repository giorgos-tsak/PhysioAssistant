package uom.android.physioassistant.backend.requests;

public class CreateDoctorRequest {

    private String username;
    private String password;
    private String afm;
    private String name;
    private String address;

    public CreateDoctorRequest(String username, String password, String afm, String name, String address) {
        this.username = username;
        this.password = password;
        this.afm = afm;
        this.name = name;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
