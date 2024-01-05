package uom.android.physioassistant.ui;

import androidx.fragment.app.Fragment;

import uom.android.physioassistant.activities.admin.CreateDoctorFragment;
import uom.android.physioassistant.activities.admin.CreateServiceFragment;
import uom.android.physioassistant.activities.doctor.CreatePatientFragment;
import uom.android.physioassistant.activities.doctor.DoctorCalendarFragment;
import uom.android.physioassistant.activities.doctor.DoctorHomeFragment;
import uom.android.physioassistant.activities.doctor.NotificationsFragment;
import uom.android.physioassistant.activities.doctor.PatientHistoryFragment;
import uom.android.physioassistant.activities.doctor.PatientsFragment;
import uom.android.physioassistant.activities.doctor.appointment.AppointmentCalendarDoctorFragment;
import uom.android.physioassistant.activities.doctor.appointment.AppointmentOptionsDoctorFragment;
import uom.android.physioassistant.activities.doctor.appointment.AppointmentSummaryDoctorFragment;
import uom.android.physioassistant.activities.patient.PatientCalendarFragment;
import uom.android.physioassistant.activities.patient.PatientHomeFragment;
import uom.android.physioassistant.activities.patient.ServiceFragment;
import uom.android.physioassistant.activities.patient.appointment.AppointmentCalendarFragment;
import uom.android.physioassistant.activities.patient.appointment.AppointmentOptionsFragment;
import uom.android.physioassistant.activities.patient.appointment.AppointmentSummary;

public enum FragmentType {

    PATIENT_HOME_FRAGMENT(new PatientHomeFragment()),
    CALENDAR_FRAGMENT(new PatientCalendarFragment()),
    SERVICE_FRAGMENT(new ServiceFragment()),
    APPOINTMENT_OPTIONS_FRAGMENT(new AppointmentOptionsFragment()),
    APPOINTMENT_CALENDAR_FRAGMENT(new AppointmentCalendarFragment()),
    APPOINTMENT_SUMMARY(new AppointmentSummary()),

    DOCTOR_HOME_FRAGMENT(new DoctorHomeFragment()),
    DOCTOR_PATIENTS_FRAGMENT(new PatientsFragment()),
    DOCTOR_CALENDAR_FRAGMENT(new DoctorCalendarFragment()),
    DOCTOR_NOTIFICATIONS_FRAGMENT(new NotificationsFragment()),
    PATIENT_HISTORY_FRAGMENT(new PatientHistoryFragment()),
    CREATE_PATIENT_FRAGMENT(new CreatePatientFragment()),
    APPOINTMENT_OPTIONS_DOCTOR_FRAGMENT(new AppointmentOptionsDoctorFragment()),
    APPOINTMENT_CALENDAR_DOCTOR_FRAGMENT(new AppointmentCalendarDoctorFragment()),
    APPOINTMENT_DOCTOR_SUMMARY(new AppointmentSummaryDoctorFragment()),

    CREATE_DOCTOR_FRAGMENT(new CreateDoctorFragment()),
    CREATE_SERVICE_FRAGMENT(new CreateServiceFragment());



    private Fragment fragment;

    FragmentType(Fragment fragment){
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
