package uom.android.physioassistant.activities.doctor;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.adapters.PatientAdapter;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.PatientsLoadedEvent;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private PatientAdapter patientAdapter;
    private FloatingActionButton createPatientButton;
    private Doctor doctor;

    public PatientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientsFragment newInstance(String param1, String param2) {
        PatientsFragment fragment = new PatientsFragment();
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
        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        initViews(view);

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctor = doctorActivity.getDoctor();

        DataManager dataManager = new DataManager();
        dataManager.loadAllPatientByDoctorId(doctorActivity.getDoctor().getAfm());

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
    public void onPatientsLoaded(PatientsLoadedEvent event){
        doctor.setPatients((ArrayList<Patient>) event.getPatients());
        initAdapter();
    }

    private void initViews(View view) {

        createPatientButton = view.findViewById(R.id.createPatientButton);
        createPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorActivity doctorActivity = (DoctorActivity) getActivity();
                doctorActivity.replaceFragment(FragmentType.CREATE_PATIENT_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out);
            }
        });

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    private void filterList(String newText) {
        ArrayList<Patient> filteredPatients = new ArrayList<>();
        for(Patient patient:doctor.getPatients()){
            if(patient.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredPatients.add(patient);
            }
        }
        if(filteredPatients.size()>0){
            patientAdapter.setPatients(filteredPatients);
        }

    }

    private void initAdapter() {
        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        patientAdapter = new PatientAdapter();
        patientAdapter.setPatients(doctorActivity.getDoctor().getPatients());

        recyclerView.setAdapter(patientAdapter);

        patientAdapter.setOnItemClickListener(new PatientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient patient) {

                doctorActivity.replaceFragment(FragmentType.PATIENT_HISTORY_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out,patientBundle(patient));
            }
        });

    }


    private Bundle patientBundle(Patient patient){
        Bundle bundle = new Bundle();
        bundle.putSerializable("patient",patient);
        return bundle;
    }



}