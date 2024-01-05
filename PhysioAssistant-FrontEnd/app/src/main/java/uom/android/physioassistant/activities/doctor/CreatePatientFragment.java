package uom.android.physioassistant.activities.doctor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.DoctorCreatedEvent;
import uom.android.physioassistant.backend.events.PatientCreatedEvent;
import uom.android.physioassistant.backend.events.PatientsLoadedEvent;
import uom.android.physioassistant.backend.requests.CreatePatientRequest;
import uom.android.physioassistant.backend.responses.ErrorResponse;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePatientFragment extends Fragment implements OnBackPressedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText nameText;
    private EditText addressText;
    private EditText amkaText;
    private TextView nameWarning,addressWarning,amkaWarning;
    private MaterialButton createButton;
    private Doctor doctor;
    private View view;

    public CreatePatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePatientFragment newInstance(String param1, String param2) {
        CreatePatientFragment fragment = new CreatePatientFragment();
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

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctorActivity.getNavBar().setVisibility(View.GONE);

        doctor = doctorActivity.getDoctor();

        view = inflater.inflate(R.layout.fragment_create_patient, container, false);

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
    public void onPatientCreated(PatientCreatedEvent event){
        Patient patient = event.getPatient();
        if(patient.getDoctors().size()==1){
            createNewPatientDialog(view,patient);
        }
        else{
            createSuccessDialogs(view);

        }
        emptyEditTexts();
        returnToPatientsFragment();
    }



    private void initViews(View view) {



        nameText = view.findViewById(R.id.nameText);
        addressText = view.findViewById(R.id.addressText);
        amkaText = view.findViewById(R.id.amkaText);

        nameWarning = view.findViewById(R.id.nameWarning);
        addressWarning = view.findViewById(R.id.addressWarning);
        amkaWarning = view.findViewById(R.id.amkaWarning);

        createButton = view.findViewById(R.id.createPatientButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFilled()){
                    if(!patientExists(createPatientRequest())){
                        DataManager dataManager = new DataManager();
                        dataManager.createPatient(doctor.getAfm(),createPatientRequest());
                    }
                }
            }
        });

    }

    private boolean patientExists(CreatePatientRequest patientRequest) {

        for(Patient patient: doctor.getPatients()){
            if(patient.getAmka().equals(patientRequest.getAmka())){
                amkaWarning.setVisibility(View.VISIBLE);
                amkaWarning.setText("*Ο ασθενής με ΑΜΚΑ "+patient.getAmka()+" υπάρχει ήδη");
                return true;
            }
        }
        return false;
    }



    private CreatePatientRequest createPatientRequest(){
        String amka = amkaText.getText().toString();
        String name = nameText.getText().toString();
        String address = addressText.getText().toString();

        return new CreatePatientRequest(amka,name,address);
    }

    private void emptyEditTexts(){
        amkaText.setText("");
        nameText.setText("");
        addressText.setText("");
    }

    private boolean isAllFilled(){
        String amka = amkaText.getText().toString();
        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        boolean allFilled=true;
        amkaWarning.setVisibility(View.INVISIBLE);
        nameWarning.setVisibility(View.INVISIBLE);
        addressWarning.setVisibility(View.INVISIBLE);


        if(amka.equals("")){
            amkaWarning.setVisibility(View.VISIBLE);
            amkaWarning.setText("*Συμπληρώστε το ΑΜΚΑ");
            allFilled = false;
        }
        if(name.equals("")){
            nameWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }
        if(address.equals("")){
            addressWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }
        return allFilled;
    }

    private void createSuccessDialogs(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Επιτυχία")
                .setMessage("Ο ασθενής δημιουργήθηκε επιτυχώς")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform any action if needed
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createNewPatientDialog(View view,Patient patient){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Επιτυχία")
                .setMessage("Ο ασθενής δημιουργήθηκε επιτυχώς και πρόκειται για νέο ασθενη.\nUsername: "+patient.getUsername()+"\nPassword: "+patient.getPassword())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform any action if needed
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void returnToPatientsFragment() {
        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctorActivity.replaceFragment(FragmentType.DOCTOR_PATIENTS_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out);
        doctorActivity.getNavBar().setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed(FragmentNavigation navigation) {

        emptyEditTexts();
        DoctorActivity doctorActivity = (DoctorActivity) navigation;
        doctorActivity.replaceFragment(FragmentType.DOCTOR_PATIENTS_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out);
        doctorActivity.getNavBar().setVisibility(View.VISIBLE);
    }
}