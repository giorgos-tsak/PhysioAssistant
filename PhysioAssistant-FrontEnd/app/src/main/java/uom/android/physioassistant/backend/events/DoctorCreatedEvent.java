package uom.android.physioassistant.backend.events;

import uom.android.physioassistant.models.Doctor;

public class DoctorCreatedEvent {

    private Doctor doctor;

    public DoctorCreatedEvent(Doctor doctor) {
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}
