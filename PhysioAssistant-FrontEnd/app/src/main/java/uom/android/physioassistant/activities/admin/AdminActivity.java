package uom.android.physioassistant.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Stack;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.DoctorsLoadedEvent;
import uom.android.physioassistant.backend.events.PhysioActionsLoadedEvent;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.ui.FragmentType;
import uom.android.physioassistant.ui.NavButton;

public class AdminActivity extends AppCompatActivity implements FragmentNavigation {


    private ArrayList<Doctor> doctors;
    private ArrayList<PhysioAction> physioActions;
    private boolean isDoctorsLoaded,isPhysioActionsLoaded;
    private Stack<Fragment> backStack = new Stack<>();
    private MaterialButton doctorButton,physioActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        DataManager dataManager = new DataManager();
        dataManager.loadDoctors();

        initViews();


    }

    private void initViews(){
        doctorButton = findViewById(R.id.physioButton);
        physioActionButton = findViewById(R.id.serviceButton);

        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonPressed(doctorButton);
                setButtonIdle(physioActionButton);
                ClearEditTexts clearEditTexts = (ClearEditTexts) FragmentType.CREATE_SERVICE_FRAGMENT.getFragment();
                clearEditTexts.clearEditTexts();
                replaceFragment(FragmentType.CREATE_DOCTOR_FRAGMENT.getFragment(), 0,0);
            }
        });

        physioActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setButtonPressed(physioActionButton);
                setButtonIdle(doctorButton);
                ClearEditTexts clearEditTexts = (ClearEditTexts) FragmentType.CREATE_DOCTOR_FRAGMENT.getFragment();
                clearEditTexts.clearEditTexts();
                replaceFragment(FragmentType.CREATE_SERVICE_FRAGMENT.getFragment(), 0,0);
            }
        });

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
    public void onDoctorsLoaded(DoctorsLoadedEvent event){
        if(!isDoctorsLoaded){
            doctors = (ArrayList<Doctor>) event.getDoctors();
            isDoctorsLoaded = true;
            DataManager dataManager = new DataManager();
            dataManager.loadPhysioActions();
            checkAllDataLoaded();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhysioActionsLoaded(PhysioActionsLoadedEvent event){
        if(!isPhysioActionsLoaded){
            physioActions = (ArrayList<PhysioAction>) event.getPhysioActions();
            isPhysioActionsLoaded = true;
            checkAllDataLoaded();
        }
    }



    @Override
    public void replaceFragment(Fragment fragment, int enterAnimation, int exitAnimation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().
                setCustomAnimations(enterAnimation,exitAnimation);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void replaceFragment(Fragment fragment, int enterAnimation, int exitAnimation, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().
                setCustomAnimations(enterAnimation,exitAnimation);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void checkAllDataLoaded(){
        if(isDoctorsLoaded && isPhysioActionsLoaded){
            replaceFragment(FragmentType.CREATE_DOCTOR_FRAGMENT.getFragment(), 0,0);
        }
    }

    public void setButtonPressed(MaterialButton button){
        button.setBackgroundColor(ContextCompat.getColor(this,R.color.blue));
        button.setTextColor(ContextCompat.getColor(this,R.color.white));
    }

    public void setButtonIdle(MaterialButton button){
        button.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        button.setTextColor(ContextCompat.getColor(this,R.color.blue));
    }

    @Override
    public void onBackPressed() {


        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        System.out.println(backStackEntryCount);
        if(backStackEntryCount==1){
            moveTaskToBack(true);
        }
        else{
            fragmentManager.popBackStack();
        }
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public ArrayList<PhysioAction> getPhysioActions() {
        return physioActions;
    }

    public MaterialButton getDoctorButton() {
        return doctorButton;
    }

    public MaterialButton getPhysioActionButton() {
        return physioActionButton;
    }
}