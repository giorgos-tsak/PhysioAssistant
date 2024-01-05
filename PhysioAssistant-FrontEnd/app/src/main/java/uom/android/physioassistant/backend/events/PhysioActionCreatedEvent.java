package uom.android.physioassistant.backend.events;

import uom.android.physioassistant.models.PhysioAction;

public class PhysioActionCreatedEvent {

    private PhysioAction physioAction;

    public PhysioActionCreatedEvent(PhysioAction physioAction) {
        this.physioAction = physioAction;
    }

    public PhysioAction getPhysioAction() {
        return physioAction;
    }
}
