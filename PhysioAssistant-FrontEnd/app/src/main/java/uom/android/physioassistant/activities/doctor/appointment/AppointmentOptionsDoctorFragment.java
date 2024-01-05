package uom.android.physioassistant.activities.doctor.appointment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.adapters.PatientDropdownAdapter;
import uom.android.physioassistant.adapters.ServiceDropDownAdapterAdapter;
import uom.android.physioassistant.activities.doctor.DoctorActivity;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.ui.DropDown;
import uom.android.physioassistant.ui.DropDownManager;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentOptionsDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentOptionsDoctorFragment extends Fragment implements OnBackPressedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialButton nextButton;

    private DropDown patientDropdown;
    private DropDown serviceDropDown;
    private ServiceDropDownAdapterAdapter serviceDropDownAdapter;
    private PatientDropdownAdapter patientDropdownAdapter;
    private DropDownManager dropDownManager;
    private ArrayList<PhysioAction> services;
    private ArrayList<Patient> patients;

    public AppointmentOptionsDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentOptionsDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentOptionsDoctorFragment newInstance(String param1, String param2) {
        AppointmentOptionsDoctorFragment fragment = new AppointmentOptionsDoctorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_options_doctor, container, false);

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctorActivity.getNavBar().setVisibility(View.GONE);

        initViews(view);

        return view;
    }

    private void initViews(View view){

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();

        services = doctorActivity.getPhysioActions();
        patients = doctorActivity.getDoctor().getPatients();

        initPatientDropdown(view);
        initServiceDropdown(view);
        initDropdownManager();

        nextButton = view.findViewById(R.id.createPatientButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dropDownManager.isAllFilled()){
                    doctorActivity.replaceFragment(FragmentType.APPOINTMENT_CALENDAR_DOCTOR_FRAGMENT.getFragment(), R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                            optionsBundle(doctorActivity.getDoctor(),dropDownManager.getSelectedPatient(),dropDownManager.getSelectedService()));
                }
                else{
                    showDialog(view);
                }
            }
        });

    }

    private void initPatientDropdown(View view){
        patientDropdown = view.findViewById(R.id.patientDropdown);
        patientDropdownAdapter = new PatientDropdownAdapter();
        patientDropdownAdapter.setPatients(patients);
        patientDropdown.setAdapter(patientDropdownAdapter);
    }

    private void initServiceDropdown(View view){
        serviceDropDown = view.findViewById(R.id.serviceDropdown);
        serviceDropDownAdapter = new ServiceDropDownAdapterAdapter(view.getContext());
        System.out.println("Services "+services);
        serviceDropDownAdapter.setServices(services);
        serviceDropDown.setAdapter(serviceDropDownAdapter);
    }

    private void initDropdownManager(){
        dropDownManager = new DropDownManager();
        dropDownManager.addDropdown(patientDropdown);
        dropDownManager.addDropdown(serviceDropDown);
        dropDownManager.handleClicks();
    }

    public void showDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Ελλιπής Στοιχεία").setMessage("Συμπληρώστε όλα τα πεδία").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public Bundle optionsBundle(Doctor doctor, Patient patient, PhysioAction physioAction){
        Bundle bundle = new Bundle();
        bundle.putSerializable("doctor",doctor);
        bundle.putSerializable("patient",patient);
        bundle.putSerializable("physio_action",physioAction);
        return bundle;
    }

    @Override
    public void onBackPressed(FragmentNavigation navigation) {
        DoctorActivity doctorActivity = (DoctorActivity) navigation;
        doctorActivity.replaceFragment(FragmentType.DOCTOR_CALENDAR_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out);
    }
}