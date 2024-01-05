package uom.android.physioassistant.backend.events;

import java.util.List;

import uom.android.physioassistant.models.Doctor;

public class DoctorsLoadedEvent {

    private List<Doctor> doctors;

    public DoctorsLoadedEvent(List<Doctor> doctors){
        this.doctors = doctors;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }
}
