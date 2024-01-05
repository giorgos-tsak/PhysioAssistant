package uom.android.physioassistant.backend.events;

import java.util.List;

import uom.android.physioassistant.models.Appointment;

public class AppointmentsLoadedEvent {
    private List<Appointment> appointments;

    public AppointmentsLoadedEvent(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}