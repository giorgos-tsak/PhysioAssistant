package uom.android.physioassistant.backend.events;

import uom.android.physioassistant.models.Patient;

public class PatientCreatedEvent {

    private Patient patient;

    public PatientCreatedEvent(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }
}
