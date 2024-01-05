package uom.android.physioassistant.activities.doctor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.adapters.AppointmentAdapter;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.comparators.AppointmentDescendingComp;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientHistoryFragment extends Fragment implements OnBackPressedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private AppointmentAdapter appointmentAdapter;
    private Patient patient;
    private TextView warningText;

    public PatientHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientHistoryFragment newInstance(String param1, String param2) {
        PatientHistoryFragment fragment = new PatientHistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_patient_history, container, false);

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctorActivity.getNavBar().setVisibility(View.GONE);
        Doctor doctor = doctorActivity.getDoctor();

        Bundle bundle = getArguments();
        if (bundle != null) {
            patient = (Patient) bundle.getSerializable("patient");
        }

        DataManager dataManager = new DataManager();
        dataManager.loadAppointmentsForPatientWithDoctor(patient.getAmka(),doctor.getAfm());
        initViews(view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPatientAppointmentsLoaded(AppointmentsLoadedEvent event){
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) event.getAppointments();
        patient.setAppointments(appointments);
        if(patient.getAppointments().size()>0){
            updateRecyclerView();
        }
        else{
            showNoAppointments(patient.getName());
        }
    }

    private void initViews(View view){

        warningText = view.findViewById(R.id.warningText);
        appointmentAdapter = new AppointmentAdapter(view.getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    private void updateRecyclerView(){

        warningText.setVisibility(View.INVISIBLE);
        Collections.sort(patient.getAppointments(),new AppointmentDescendingComp());
        appointmentAdapter.setAppointments(patient.getAppointments());
        recyclerView.setAdapter(appointmentAdapter);
    }

    private void showNoAppointments(String patientName) {
        recyclerView.setVisibility(View.INVISIBLE);

        warningText.setVisibility(View.VISIBLE);
        warningText.setText("Ο "+patientName+" δεν έχει κλήσει κανένα ραντεβού");
    }



    @Override
    public void onBackPressed(FragmentNavigation navigation) {
        DoctorActivity doctorActivity = (DoctorActivity) navigation;
        doctorActivity.replaceFragment(FragmentType.DOCTOR_PATIENTS_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out);
        doctorActivity.getNavBar().setVisibility(View.VISIBLE);
    }
}