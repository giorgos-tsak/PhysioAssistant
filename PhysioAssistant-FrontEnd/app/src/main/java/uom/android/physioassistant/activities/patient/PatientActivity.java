package uom.android.physioassistant.activities.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.R;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.backend.events.DoctorsLoadedEvent;
import uom.android.physioassistant.backend.events.PatientLoadedEvent;
import uom.android.physioassistant.backend.events.PhysioActionsLoadedEvent;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.models.User;
import uom.android.physioassistant.ui.FragmentType;
import uom.android.physioassistant.ui.PatientNavBar;

public class PatientActivity extends AppCompatActivity implements FragmentNavigation {


    private PatientNavBar navBar;
    private User user;
    private ArrayList<PhysioAction> physioActions;
    private ArrayList<Doctor> doctors;
    public Patient patient;

    private boolean isPatientLoaded,isAppointmentsLoaded,isDoctorsLoaded,isPhysioActionsLoaded,initActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);


        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");


        DataManager dataManager = new DataManager();
        dataManager.loadPatientByUsername(user.getUsername());

    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPatientLoaded(PatientLoadedEvent event){
        if(!isPatientLoaded){
            patient = event.getPatient();
            isPatientLoaded = true;
            DataManager dataManager = new DataManager();
            dataManager.loadAppointmentsByPatientId(patient.getAmka());
            checkIfAllDataLoaded();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPatientAppointmentsLoaded(AppointmentsLoadedEvent event){
        if(!isAppointmentsLoaded){
            ArrayList<Appointment> appointments = (ArrayList<Appointment>) event.getAppointments();
            patient.setAppointments(appointments);
            isAppointmentsLoaded = true;
            DataManager dataManager = new DataManager();
            dataManager.loadDoctorsByPatientId(patient.getAmka());
            checkIfAllDataLoaded();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoctorsLoaded(DoctorsLoadedEvent event){
        if(!isDoctorsLoaded){
            doctors = (ArrayList<Doctor>) event.getDoctors();
            isDoctorsLoaded = true;
            System.out.println(patient.getDoctors());
            DataManager dataManager = new DataManager();
            dataManager.loadPhysioActions();
            checkIfAllDataLoaded();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhysioActionsLoaded(PhysioActionsLoadedEvent event){
        if(!isPhysioActionsLoaded){
            physioActions = (ArrayList<PhysioAction>) event.getPhysioActions();
            isPhysioActionsLoaded = true;
            checkIfAllDataLoaded();
        }
    }

    private void checkIfAllDataLoaded() {
        if (isPatientLoaded  && isAppointmentsLoaded && isDoctorsLoaded && isPhysioActionsLoaded) {
            if(!initActivity){
                initActivity=true;
                initActivity();
            }
        }
    }

    public void initActivity(){
        replaceFragment(FragmentType.PATIENT_HOME_FRAGMENT.getFragment(),0,0);
        navBar = findViewById(R.id.navBar);
        navBar.setActivity(this);
        navBar.handleClicks();
    }

    @Override
    public void replaceFragment(Fragment fragment,int enterAnimation,int exitAnimation){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().
                setCustomAnimations(enterAnimation,exitAnimation);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void replaceFragment(Fragment fragment,int enterAnimation,int exitAnimation,Bundle bundle){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().
                setCustomAnimations(enterAnimation,exitAnimation);
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if(fragment instanceof OnBackPressedListener){
            ((OnBackPressedListener) fragment).onBackPressed(this);
        }
        else{
            navBar.undo();
        }
    }

    public PatientNavBar getNavBar() {
        return navBar;
    }

    public Patient getPatient() {
        return patient;
    }

    public ArrayList<PhysioAction> getPhysioActions() {
        return physioActions;
    }


    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }
}