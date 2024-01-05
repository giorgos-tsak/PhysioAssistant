package uom.android.physioassistant.backend.requests;

public class CreatePatientRequest {
    private String amka;
    private String name;
    private String address;

    public CreatePatientRequest(String amka, String name, String address) {
        this.amka = amka;
        this.name = name;
        this.address = address;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
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
