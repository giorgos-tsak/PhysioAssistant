package uom.android.physioassistant.models.comparators;

import java.util.Comparator;

import uom.android.physioassistant.models.Appointment;

public class AppointmentAscendingComp implements Comparator<Appointment> {
    @Override
    public int compare(Appointment o1, Appointment o2) {
        if(o1.getLocalDate().compareTo(o2.getLocalDate())==0){
            return o1.getLocalTime().compareTo(o2.getLocalTime());
        }
        return o1.getLocalDate().compareTo(o2.getLocalDate());
    }
}
