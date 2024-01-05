package uom.android.physioassistant.backend.events;

import java.util.List;

import uom.android.physioassistant.models.PhysioAction;

public class PhysioActionsLoadedEvent {

    private List<PhysioAction> physioActions;

    public PhysioActionsLoadedEvent(List<PhysioAction> physioActions){
        this.physioActions = physioActions;
    }

    public List<PhysioAction> getPhysioActions() {
        return physioActions;
    }
}
