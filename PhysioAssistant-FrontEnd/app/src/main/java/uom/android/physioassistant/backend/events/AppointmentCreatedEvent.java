package uom.android.physioassistant.backend.events;

import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;

public class AppointmentCreatedEvent {

    private Appointment appointment;

    public AppointmentCreatedEvent(Appointment appointment){
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
