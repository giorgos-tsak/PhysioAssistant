package uom.android.physioassistant.backend.events;

public class AppointmentUpdatedEvent {
    private boolean success;

    public AppointmentUpdatedEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
