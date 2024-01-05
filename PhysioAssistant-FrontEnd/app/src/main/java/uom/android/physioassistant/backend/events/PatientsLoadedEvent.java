package uom.android.physioassistant.backend.events;

import java.util.ArrayList;
import java.util.List;

import uom.android.physioassistant.models.Patient;

public class PatientsLoadedEvent {

    private List<Patient> patients;

    public PatientsLoadedEvent(List<Patient> patients) {
        this.patients = patients;
    }

    public List<Patient> getPatients() {
        return patients;
    }
}
