package uom.android.physioassistant.models.comparators;

import java.util.Comparator;

import uom.android.physioassistant.models.Appointment;

public class AppointmentDescendingComp implements Comparator<Appointment> {
    @Override
    public int compare(Appointment o1, Appointment o2) {
        if(o2.getLocalDate().compareTo(o1.getLocalDate())==0){
            return o2.getLocalTime().compareTo(o1.getLocalTime());
        }
        return o2.getLocalDate().compareTo(o1.getLocalDate());
    }
}
