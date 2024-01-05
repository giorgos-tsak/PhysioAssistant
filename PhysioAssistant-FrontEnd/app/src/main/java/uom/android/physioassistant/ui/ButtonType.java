package uom.android.physioassistant.ui;

import androidx.fragment.app.Fragment;

import uom.android.physioassistant.R;

public enum ButtonType {

    PATIENT_HOME(R.drawable.ic_home,R.drawable.ic_home_pressed,FragmentType.PATIENT_HOME_FRAGMENT),
    PATIENT_CALENDAR(R.drawable.ic_calendar,R.drawable.ic_calendar_pressed,FragmentType.CALENDAR_FRAGMENT),
    DOCTOR_HOME(R.drawable.ic_home,R.drawable.ic_home_pressed,FragmentType.DOCTOR_HOME_FRAGMENT),
    DOCTOR_CALENDAR(R.drawable.ic_calendar,R.drawable.ic_calendar_pressed,FragmentType.DOCTOR_CALENDAR_FRAGMENT),
    DOCTOR_PATIENTS(R.drawable.ic_profile,R.drawable.ic_profile_pressed,FragmentType.DOCTOR_PATIENTS_FRAGMENT),
    DOCTOR_NOTIFICATIONS(R.drawable.ic_notification,R.drawable.ic_notification_pressed,FragmentType.DOCTOR_NOTIFICATIONS_FRAGMENT);



    private int pressed,idle;
    private FragmentType fragmentType;
    ButtonType(int idle,int pressed,FragmentType fragmentType){
        this.idle =idle;
        this.pressed = pressed;
        this.fragmentType = fragmentType;
    }

    public int getPressed() {
        return pressed;
    }

    public int getIdle() {
        return idle;
    }

    public Fragment getFragment() {
        return fragmentType.getFragment();
    }
}
